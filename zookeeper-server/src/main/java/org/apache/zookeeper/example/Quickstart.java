package org.apache.zookeeper.example;

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
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.IOException;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.client.ZKClientConfig;
import org.apache.zookeeper.client.ZooKeeperBuilder;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.embedded.ZKBridgeClusterEmbedded;
import org.apache.zookeeper.server.embedded.ZKBridgeClusterEmbedded.ZKBridgeClusterEmbeddedBuilder;
import org.apache.zookeeper.server.embedded.spiral.InMemoryFS;
import org.apache.zookeeper.server.embedded.spiral.InMemorySpiralClient;
import org.apache.zookeeper.spiral.SpiralBucket;
import org.apache.zookeeper.spiral.SpiralClient;
import org.junit.Assert;


public class Quickstart {

  private static final Integer SESSION_TIMEOUT_MS = 10_000; // 10 sec

  private static ZooKeeper s1Client;
  private static ZooKeeper s2Client;

  public static void main(String[] args) throws Exception {
    // create auto-closable cluster
    ZKBridgeClusterEmbedded zkBridgeCluster = new ZKBridgeClusterEmbeddedBuilder().setNumServers(3).build();
    InMemoryFS inMemoryFS = zkBridgeCluster.getInMemoryFS();
    SpiralClient inMemSpiralClient = new InMemorySpiralClient(inMemoryFS);

    try {
      // validate session creation and sync
      validateSessionCreationAndSync(zkBridgeCluster, inMemSpiralClient);

      // validate data creation and sync
      validateDataCreationAndSync(zkBridgeCluster, inMemSpiralClient);

    } finally {
      zkBridgeCluster.close();
      s1Client.close();
      s2Client.close();
    }

    System.exit(0);
  }

  private static void validateSessionCreationAndSync(ZKBridgeClusterEmbedded zkBridgeCluster, SpiralClient spiralClient)
      throws Exception {
    // connect client-1 with server-1
    s1Client = buildClientForServer(zkBridgeCluster, 0);
    assertSessionSize(spiralClient, 1);

    // connect client-2 with server-2
    s2Client = buildClientForServer(zkBridgeCluster, 1);
    assertSessionSize(spiralClient, 2);
  }

  private static void validateDataCreationAndSync(ZKBridgeClusterEmbedded zkBridgeCluster, SpiralClient spiralClient)
      throws Exception {
    // create data with client-1 and validate with client-2
    byte[] bytesCreated = "hello".getBytes();
    s1Client.create("/cluster1", bytesCreated, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    Thread.sleep(10000);
    byte[] responseData = s2Client.getData("/cluster1", false, new Stat());
    Assert.assertEquals("data not present", bytesCreated, responseData);

    // create data with client-2 and validate with client-1
    byte[] bytesCreated2 = "world".getBytes();
    s2Client.create("/cluster2", bytesCreated, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    Thread.sleep(10000);
    byte[] responseData2 = s2Client.getData("/cluster2", false, new Stat());
    Assert.assertEquals("data not present", bytesCreated2, responseData2);
  }

  private static ZooKeeper buildClientForServer(ZKBridgeClusterEmbedded zkBridgeCluster, int serverId) throws Exception {
    ZooKeeper client = new ZooKeeperBuilder(zkBridgeCluster.getConnectionString(serverId), SESSION_TIMEOUT_MS)
        .withDefaultWatcher(new SimpleWatcher())
        .withClientConfig(new ZKClientConfig())
        .build();

    while (client.getState() != ZooKeeper.States.CONNECTED) {
      Thread.sleep(100);
    }
    return client;
  }

  private static void assertSessionSize(SpiralClient spiralClient, int expected) {
    Assert.assertEquals(
        expected,
        spiralClient.scanBucket(SpiralBucket.SESSIONS.getBucketName(), null).getKeyValuesCount());
  }


}
