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

package org.apache.zookeeper.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.jute.BinaryInputArchive;
import org.apache.jute.BinaryOutputArchive;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZKBEnableDisableTest;
import org.apache.zookeeper.ZKTestCase;
import org.apache.zookeeper.server.embedded.spiral.SpiralClientStrategy.InMemorySpiralClientStrategy;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig.ConfigException;
import org.apache.zookeeper.spiral.SpiralClient;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeserializationPerfTest extends ZKTestCase {

    protected static final Logger LOG = LoggerFactory.getLogger(DeserializationPerfTest.class);

    private static void deserializeTree(int depth, int width, int len, boolean spiralEnabled) throws InterruptedException, IOException, KeeperException.NodeExistsException, KeeperException.NoNodeException, ConfigException {
        BinaryInputArchive ia = null;
        int count;
        SpiralClient spiralClient = null;
        {
            DataTree tree = new DataTree();
            SerializationPerfTest.createNodes(tree, "/", depth, width, tree.getNode("/").stat.getCversion(), new byte[len]);
            count = tree.getNodeCount();

            if (spiralEnabled) {
                spiralClient = (new InMemorySpiralClientStrategy()).buildSpiralClient();
                tree.serializeOnSpiral(spiralClient, "nodaDataBucket", "aclCacheBucket");
            } else {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                BinaryOutputArchive oa = BinaryOutputArchive.getArchive(baos);
                tree.serialize(oa, "test");
                baos.flush();

                ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                ia = BinaryInputArchive.getArchive(bais);
            }
        }

        DataTree dserTree = new DataTree();

        System.gc();
        long start = System.nanoTime();
        if (spiralEnabled) {
            dserTree.deserializeFromSpiral(spiralClient, "nodaDataBucket", "aclCacheBucket");
        } else {
            dserTree.deserialize(ia, "test");
        }
        long end = System.nanoTime();
        long durationms = (end - start) / 1000000L;
        long pernodeus = ((end - start) / 1000L) / count;

        assertEquals(count, dserTree.getNodeCount());

        LOG.info(
            "Deserialized {} nodes in {} ms ({}us/node), depth={} width={} datalen={}",
            count,
            durationms,
            pernodeus,
            depth,
            width,
            len);
    }

    @ZKBEnableDisableTest
    public void testSingleDeserialize(boolean spiralEnabled) throws InterruptedException, IOException, KeeperException.NodeExistsException, KeeperException.NoNodeException, ConfigException {
        deserializeTree(1, 0, 20, spiralEnabled);
    }

    @ZKBEnableDisableTest
    public void testWideDeserialize(boolean spiralEnabled) throws InterruptedException, IOException, KeeperException.NodeExistsException, KeeperException.NoNodeException, ConfigException {
        deserializeTree(2, 10000, 20, spiralEnabled);
    }

    @ZKBEnableDisableTest
    public void testDeepDeserialize(boolean spiralEnabled) throws InterruptedException, IOException, KeeperException.NodeExistsException, KeeperException.NoNodeException, ConfigException {
        deserializeTree(400, 1, 20, spiralEnabled);
    }

    @ZKBEnableDisableTest
    public void test10Wide5DeepDeserialize(boolean spiralEnabled) throws InterruptedException, IOException, KeeperException.NodeExistsException, KeeperException.NoNodeException, ConfigException {
        deserializeTree(5, 10, 20, spiralEnabled);
    }

    @ZKBEnableDisableTest
    public void test15Wide5DeepDeserialize(boolean spiralEnabled) throws InterruptedException, IOException, KeeperException.NodeExistsException, KeeperException.NoNodeException, ConfigException {
        deserializeTree(5, 15, 20, spiralEnabled);
    }

    @ZKBEnableDisableTest
    public void test25Wide4DeepDeserialize(boolean spiralEnabled) throws InterruptedException, IOException, KeeperException.NodeExistsException, KeeperException.NoNodeException, ConfigException {
        deserializeTree(4, 25, 20, spiralEnabled);
    }

    @ZKBEnableDisableTest
    public void test40Wide4DeepDeserialize(boolean spiralEnabled) throws InterruptedException, IOException, KeeperException.NodeExistsException, KeeperException.NoNodeException, ConfigException {
        deserializeTree(4, 40, 20, spiralEnabled);
    }

    @ZKBEnableDisableTest
    public void test300Wide3DeepDeserialize(boolean spiralEnabled) throws InterruptedException, IOException, KeeperException.NodeExistsException, KeeperException.NoNodeException, ConfigException {
        deserializeTree(3, 300, 20, spiralEnabled);
    }

}
