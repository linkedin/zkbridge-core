package org.apache.zookeeper.server.cluster;

import org.apache.zookeeper.ZKBTest;
import org.apache.zookeeper.ZooKeeper;
import org.junit.jupiter.api.Test;

import static org.apache.zookeeper.ZooKeeper.States.*;
import static org.junit.jupiter.api.Assertions.*;


@ZKBTest
public class ZKBClusterSessionConnectionTest extends ClusterTestBase {

  @Test
  void testValidateSessionCreation() throws InterruptedException {
    launchServers(3);

    // connect client-1 with server-0
    ZooKeeper client1 = cluster.getOrBuildClient(0);
    waitForOne(client1, CONNECTED);
    assertSessionSize(1);

    // connect client-2 with server-0
    ZooKeeper client2 = cluster.getOrBuildClient(1);
    waitForOne(client2, CONNECTED);
    assertSessionSize(2);
  }

  @Test
  void testValidateSessionRestorationWhenServerRestart() throws Exception {
    launchServers(3);

    // connect client-1 with server-0
    ZooKeeper client1 = cluster.getOrBuildClient(0);
    waitForOne(client1, CONNECTED);
    assertSessionSize(1);

    long sessionId = client1.getSessionId();

    // restart ZKB server-0
    cluster.restartServer(0);

    // ensure client-1 is still connected with server-0
    assertEquals(CONNECTED, client1.getState());
    assertEquals(sessionId, client1.getSessionId());
  }

  @Test
  void testValidateSessionWithoutFailoverWhenPrimaryServerFails() throws Exception {
    int sessionTimeoutMs = 2_000;
    launchServers(3, sessionTimeoutMs);

    // connect client-1 with server-0, without failover servers
    ZooKeeper client1 = cluster.getOrBuildClient(0);
    waitForOne(client1, CONNECTED);
    assertSessionSize(1);

    // shutdown ZKB server-0
    cluster.shutdownServer(0);
    Thread.sleep(sessionTimeoutMs);

    // ensure client-1 is not able to stay connected.
    assertFalse(client1.getState().isConnected());
  }

  @Test
  void testValidateSessionFailoverWhenPrimaryServerFails() throws Exception {
    int sessionTimeoutMs = 2_000;
    launchServers(3, sessionTimeoutMs);

    // connect client-1 with server-0, failover servers: server-1
    ZooKeeper client1 = cluster.getOrBuildClient(0, new int[] { 1 } /* failover server Ids */);
    waitForOne(client1, CONNECTED);
    assertSessionSize(1);

    long sessionId = client1.getSessionId();

    // shutdown ZKB server-0
    cluster.shutdownServer(0);
    Thread.sleep(sessionTimeoutMs);

    // ensure client-1 is still connected with server-1
    assertTrue(client1.getState().isConnected());
    assertEquals(CONNECTED, client1.getState());
    assertEquals(sessionId, client1.getSessionId());
  }

}
