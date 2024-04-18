package org.apache.zookeeper.server.cluster;

import static org.apache.zookeeper.ZooKeeper.States.CONNECTED;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZKBTest;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.server.embedded.ZKBridgeClusterEmbedded;
import org.junit.jupiter.api.Test;

@ZKBTest
public class ZKBClusterEphemeralNodeTest extends ClusterTestBase {
    @Test
  void testEpehemeralNodeAcrossClients() throws Exception {
    int sessionTimeoutMs = 5_000;
    ZKBridgeClusterEmbedded cluster = launchServers(3, sessionTimeoutMs);

    // connect client-1 with server-0
    ZooKeeper client1 = cluster.getOrBuildClient(0);
    waitForOne(client1, CONNECTED);
    assertSessionSize(cluster, 1);

    // connect client-2 with server-1
    ZooKeeper client2 = cluster.getOrBuildClient(1);
    waitForOne(client2, CONNECTED);
    assertSessionSize(cluster, 2);

    String nodePath = "/e1";

    // create ephemeral node on server-0
    client1.create(nodePath, "1".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
    // verify node exists on server-0
    assertNotNull(client1.exists(nodePath, false));

    // RR: TODO: In current POC if ephemeral node is created between backgrdound sync of other servers
    // then it will not be created on other servers. Need to fix this by persisting ephemeral nodes
    // info in Spiral.
    //Wait for background sync to complete on other servers.
    cluster.waitAllServersSynced(0);

    // verify node exists on server-1 as well
    assertNotNull(client2.exists(nodePath, false));

    // tear down
    client1.close();
    client2.close();
    cluster.close();
  }
  
  @Test
  void testEphemeralNodeDeletionOnSessionClose() throws Exception {
    int sessionTimeoutMs = 5_000;
    ZKBridgeClusterEmbedded cluster = launchServers(3, sessionTimeoutMs);

    // connect client-1 with server-0
    ZooKeeper client1 = cluster.getOrBuildClient(0);
    waitForOne(client1, CONNECTED);
    assertSessionSize(cluster, 1);

    // connect client-2 with server-1
    ZooKeeper client2 = cluster.getOrBuildClient(1);
    waitForOne(client2, CONNECTED);
    assertSessionSize(cluster, 2);

    String nodePath = "/e1";

    // create ephemeral node on server-0
    client1.create(nodePath, "1".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
    // verify node exists on server-0
    assertNotNull(client1.exists(nodePath, false));

    client1.close();

    // verify node is deleted on server-1 as well
    assertNull(client2.exists(nodePath, false));

    // tear down
    client1.close();
    client2.close();
    cluster.close();
  }

  @Test
  void testEphemeralNodeExistsOnHandOver() throws Exception {
    int sessionTimeoutMs = 10_000;
    ZKBridgeClusterEmbedded cluster = launchServers(3, sessionTimeoutMs);

    // connect client-1 with server-0, failover servers: server-1
    ZooKeeper client1 = cluster.getOrBuildClient(0, new int[] { 1 } /* failover server Ids */);
    waitForOne(client1, CONNECTED);
    assertSessionSize(cluster, 1);
    
    // create ephemeral node on server-0
    String nodePath = "/e1";
    client1.create(nodePath, "1".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
    // verify node exists on server-0
    assertNotNull(client1.exists(nodePath, false));

    long sessionId = client1.getSessionId();

    // shutdown ZKB server-0
    cluster.shutdownServer(0);
    Thread.sleep(sessionTimeoutMs);

    // ensure client-1 is still connected with server-1
    assertTrue(client1.getState().isConnected());
    assertEquals(CONNECTED, client1.getState());
    assertEquals(sessionId, client1.getSessionId());

    // ensure ephemeral node exists on server-1
    ZooKeeper client1HandedOver = cluster.getOrBuildClient(1);
    assertNotNull(client1HandedOver.exists(nodePath, false));

    // Tear down
    client1.close();
    cluster.close();
  }

  @Test 
  void testDuplicateEphemeralNodeOnTwoServers() throws Exception {
    int sessionTimeoutMs = 10_000;
    ZKBridgeClusterEmbedded cluster = launchServers(3, sessionTimeoutMs);

    // connect client-1 with server-0
    ZooKeeper client1 = cluster.getOrBuildClient(0);
    waitForOne(client1, CONNECTED);
    assertSessionSize(cluster, 1);

    // create ephemeral node on server-0
    String nodePath = "/e1";
    client1.create(nodePath, "1".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
    // verify node exists on server-0
    assertNotNull(client1.exists(nodePath, false));

    // connect client-2 with server-1
    ZooKeeper client2 = cluster.getOrBuildClient(1);
    waitForOne(client2, CONNECTED);
    assertSessionSize(cluster, 2);

    // RR: TODO: In current POC if ephemeral node is created between backgrdound sync of other servers
    // then it will not be created on other servers. Need to fix this by persisting ephemeral nodes
    // info in Spiral.
    //Wait for background sync to complete on other servers.
     cluster.waitAllServersSynced(0);

    // Now creating same ephemeral node on server-1 should fail with ALREADY_EXISTS error.
    try {
      client2.create(nodePath, "1".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
      fail("Should have thrown NodeExistsException");
    } catch (Exception e) {
      assertTrue(e instanceof org.apache.zookeeper.KeeperException.NodeExistsException);
    }

    // tear down
    client1.close();
    client2.close();
    cluster.close();
  }
}
