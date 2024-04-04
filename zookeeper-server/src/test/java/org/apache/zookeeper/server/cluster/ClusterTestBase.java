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

/**
 *
 */

package org.apache.zookeeper.server.cluster;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZKTestCase;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.server.embedded.ZKBridgeClusterEmbedded;
import org.apache.zookeeper.server.embedded.spiral.InMemoryFS;
import org.apache.zookeeper.server.embedded.spiral.InMemorySpiralClient;
import org.apache.zookeeper.spiral.SpiralBucket;
import org.apache.zookeeper.spiral.SpiralClient;
import org.apache.zookeeper.test.ClientBase;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Has some common functionality for tests that work with ZKB Clusters. Override
 * process(WatchedEvent) to implement the Watcher interface
 */
public class ClusterTestBase extends ZKTestCase implements Watcher {

    protected static final Logger LOG = LoggerFactory.getLogger(ClusterTestBase.class);

    protected ZKBridgeClusterEmbedded cluster;
    protected InMemoryFS inMemoryFS;
    protected SpiralClient inMemSpiralClient;

    @AfterEach
    public void tearDown() throws Exception {
        if (cluster == null) {
            LOG.info("No cluster to shutdown!");
            return;
        }
        cluster.close();
    }

    protected void launchServers(int numServers) {
        launchServers(numServers, null);
    }

    protected void launchServers(int numServers, Integer sessionTimeoutMs) {
        try {
            if (cluster != null) {
                cluster.close();
            }

            cluster = new ZKBridgeClusterEmbedded.ZKBridgeClusterEmbeddedBuilder()
                .setNumServers(numServers)
                .setSessionTimeoutMs(sessionTimeoutMs)
                .build();

            inMemoryFS = cluster.getInMemoryFS();
            inMemSpiralClient = new InMemorySpiralClient(inMemoryFS);
        } catch (Exception e) {
            throw new RuntimeException("Error while setting up ZKB Cluster", e);
        }
    }

    public void process(WatchedEvent event) {
        // ignore for this test
    }

    public static void waitForOne(ZooKeeper zk, ZooKeeper.States state) throws InterruptedException {
        int iterations = ClientBase.CONNECTION_TIMEOUT / 500;
        while (zk.getState() != state) {
            if (iterations-- == 0) {
                throw new RuntimeException("Waiting too long " + zk.getState() + " != " + state);
            }
            Thread.sleep(500);
        }
    }

    protected void waitForAll(ZooKeeper.States state) throws InterruptedException {
        waitForAll(cluster.zkClients, state);
    }

    void waitForAll(ZooKeeper[] zks, ZooKeeper.States state) throws InterruptedException {
        int iterations = ClientBase.CONNECTION_TIMEOUT / 1000;
        boolean someoneNotConnected = true;
        while (someoneNotConnected) {
            if (iterations-- == 0) {
                logStates(zks);
                ClientBase.logAllStackTraces();
                throw new RuntimeException("Waiting too long");
            }

            someoneNotConnected = false;
            for (ZooKeeper zk : zks) {
                if (zk.getState() != state) {
                    someoneNotConnected = true;
                    break;
                }
            }
            Thread.sleep(1000);
        }
    }

    public static void logStates(ZooKeeper[] zks) {
        StringBuilder sbBuilder = new StringBuilder("Connection States: {");
        for (int i = 0; i < zks.length; i++) {
            sbBuilder.append(i + " : " + zks[i].getState() + ", ");
        }
        sbBuilder.append('}');
        LOG.error(sbBuilder.toString());
    }

    public void assertSessionSize(int expectedSessions) {
        Assert.assertEquals(
            expectedSessions,
            inMemSpiralClient.scanBucket(SpiralBucket.SESSIONS.getBucketName(), null).getKeyValuesCount());
    }

}
