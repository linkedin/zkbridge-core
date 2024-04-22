/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

 package org.apache.zookeeper.server.cluster;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.Op;
import org.apache.zookeeper.ZKBTest;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooKeeper.States;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.DataTree;
import org.apache.zookeeper.server.ZKBServerConfig;
import org.apache.zookeeper.server.ZKDatabase;
import org.apache.zookeeper.server.ZooKeeperServer;
import org.apache.zookeeper.server.embedded.ZKBridgeClusterEmbedded;
import org.apache.zookeeper.server.embedded.ZKBridgeServerEmbedded.ZKBridgeServerEmbeddedBuilder;
import org.apache.zookeeper.spiral.SpiralClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test cases used to catch corner cases due to fuzzy snapshot.
 */
@ZKBTest
public class ZKBFuzzySnapshotRelatedTest extends ClusterTestBase {

    private static final Logger LOG = LoggerFactory.getLogger(ZKBFuzzySnapshotRelatedTest.class);
    private ZKBridgeClusterEmbedded cluster;

    @BeforeEach
    public void setup() throws Exception {
        // Create a custom zkbserverembeeded builder which will inherit ZKBridgeServerEmbeddedBuilder
        // and override the buildServerFromConfig method to return a ZooKeeperServer with a custom DataTree
        ZKBridgeClusterEmbedded.setBuilder(new CustomZKBServerEmbeddedBuilder());

        LOG.info("Start up a 3 server ZKBridge cluster");
        final int NUM_SERVERS = 3;
        cluster = launchServers(NUM_SERVERS);
        launchClients(cluster);
        waitForAll(cluster, States.CONNECTED);
    }

    @AfterEach
    public void tearDown() throws Exception {
        cluster.close();
    }

    @Test
    public void testMultiOpConsistency() throws Exception {
        LOG.info("Create a parent node");
        final String path = "/testMultiOpConsistency";
        ZooKeeper zk = cluster.getOrBuildClient(0);
        createEmptyNode(zk, path, CreateMode.PERSISTENT);

        LOG.info("Hook to catch the 2nd sub create node txn in multi-op");
        ZooKeeperServer zkServer = cluster.getServers().get(0).getZooKeeperServer();
        CustomDataTree dt = (CustomDataTree) zkServer.getZKDatabase().getDataTree();

        String node1 = path + "/1";
        String node2 = path + "/2";

        dt.addNodeCreateListener(node2, new NodeCreateListener() {
            @Override
            public void process(String path) {
                LOG.info("Take a snapshot");
                try {
                    zkServer.takeSnapshot(true);
                } catch (final IOException e) {
                    // ignored as it should never reach here because of System.exit() call
                }
            }
        });

        LOG.info("Issue a multi op to create 2 nodes");
        zk.multi(Arrays.asList(
                Op.create(node1, node1.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT),
                Op.create(node2, node2.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT)));

        LOG.info("Restart the server");
        zkServer.shutdown();
        waitForOne(zk, States.CONNECTING);
        
        zkServer.startup();
        waitForOne(zk, States.CONNECTED);
        LOG.info("Make sure the node consistent with leader");
        assertEquals(
                new String(cluster.getOrBuildClient(0).getData(node2, null, null)),
                new String(cluster.getOrBuildClient(1).getData(node2, null, null)));
    }
    
    /**
     * It's possible that parent node is deleted before child node and non-snap leader was also
     * not able to do background sync this change. So now when non-sync leader is rebooted and syncing 
     * from snapshot we need to make sure the pzxid get correctly updated
     * when applying the txns received.
     */
    @Test
    public void testPZxidUpdatedDuringLoadingSnapshotOnNonSnapLeader() throws Exception {
        final String parent = "/testPZxidUpdatedWhenDeletingNonExistNode";
        final String child = parent + "/child";
        int snapLeaderZKB = 0;
        int nonSnapLeaderZKB = 1;
        ZooKeeper zkClient_1 = cluster.getOrBuildClient(snapLeaderZKB);
        ZooKeeper zkClient_2 = cluster.getOrBuildClient(nonSnapLeaderZKB);
        createEmptyNode(zkClient_1, parent, CreateMode.PERSISTENT);
        createEmptyNode(zkClient_1, child, CreateMode.EPHEMERAL);
        // create another child to test closeSession
        createEmptyNode(zkClient_1, child + "1", CreateMode.EPHEMERAL);

        LOG.info("shutdown other zkbridge other zkbridge node to wipe out it's state");
        cluster.shutdownServer(nonSnapLeaderZKB);
        waitForOne(zkClient_2, States.CONNECTING);
        
        LOG.info("Set up ZKDatabase to catch the node serializing in DataTree");
        addSerializeListener(snapLeaderZKB, parent, child);
        cluster.getServers().get(snapLeaderZKB).getZooKeeperServer().takeSnapshot(true);

        LOG.info("Restart nonSnapLeaderZKB to load snapshot taken by snapLeaderZKB");
        cluster.startServer(nonSnapLeaderZKB);
        waitForOne(zkClient_2, States.CONNECTED);
    
        LOG.info("Check and make sure the pzxid of the parent is the same on both zkbridge nodes");
        compareStat(parent, snapLeaderZKB, nonSnapLeaderZKB);
    }

    private void addSerializeListener(int sid, String parent, String child) {
        final ZooKeeper zkClient = cluster.zkClients[sid];
        CustomDataTree dt = (CustomDataTree) cluster.getServers().get(sid).getZooKeeperServer().getZKDatabase().getDataTree();
        dt.addListener(parent, new NodeSerializeListener() {
            @Override
            public void nodeSerialized(String path) {
                try {
                    zkClient.delete(child, -1);
                    zkClient.close();
                    LOG.info("Deleted the child node after the parent is serialized");
                } catch (Exception e) {
                    LOG.error("Error when deleting node {}", e);
                }
            }
        });
    }

    private void compareStat(String path, int sid, int compareWithSid) throws Exception {
        ZooKeeper[] compareZk = new ZooKeeper[2];

        compareZk[0] = cluster.getOrBuildClient(sid);
        compareZk[1] = cluster.getOrBuildClient(compareWithSid);
        waitForAll(compareZk, States.CONNECTED);

        try {
            Stat stat1 = new Stat();
            compareZk[0].getData(path, null, stat1);

            Stat stat2 = new Stat();
            compareZk[1].getData(path, null, stat2);

            assertEquals(stat1, stat2);
        } finally {
            for (ZooKeeper z: compareZk) {
                z.close();
            }
        }
    }

    private void createEmptyNode(ZooKeeper zk, String path, CreateMode mode) throws Exception {
        zk.create(path, new byte[0], Ids.OPEN_ACL_UNSAFE, mode);
    }

    interface NodeCreateListener {

        void process(String path);

    }

    static class CustomDataTree extends DataTree {

        Map<String, NodeCreateListener> nodeCreateListeners = new HashMap<>();
        Map<String, NodeSerializeListener> listeners = new HashMap<>();
       
        @Override
        public void serializeSpiralNode(SpiralClient spiralClient, StringBuilder path, String nodeDataBucket) throws IOException {
            super.serializeSpiralNode(spiralClient, path, nodeDataBucket);
            NodeSerializeListener listener = listeners.get(path.toString());
            if (listener != null) {
                listener.nodeSerialized(path.toString());
            }
        }

        public void addListener(String path, NodeSerializeListener listener) {
            listeners.put(path, listener);
        }

        @Override
        public void createNode(
                final String path,
                byte[] data,
                List<ACL> acl,
                long ephemeralOwner,
                int parentCVersion,
                long zxid,
                long time,
                Stat outputStat) throws NoNodeException, NodeExistsException {
            NodeCreateListener listener = nodeCreateListeners.get(path);
            if (listener != null) {
                listener.process(path);
            }
            super.createNode(path, data, acl, ephemeralOwner, parentCVersion, zxid, time, outputStat);
        }

        public void addNodeCreateListener(String path, NodeCreateListener listener) {
            nodeCreateListeners.put(path, listener);
        }
    }

    interface NodeSerializeListener {

        void nodeSerialized(String path);

    }

    interface CommitSessionListener {

        void process(long sessionId);

    }

    static class CustomZKBServerEmbeddedBuilder extends ZKBridgeServerEmbeddedBuilder {

        @Override
        public ZooKeeperServer buildServerFromConfig(ZKBServerConfig config) throws Exception {
            ZooKeeperServer zks = super.buildServerFromConfig(config);
            zks.setZKDatabase(new ZKDatabase(zks.getTxnLogFactory()) {
                @Override
                public DataTree createDataTree() {
                    return new CustomDataTree();
                }
            });
            return zks;
        }
    }
}
