package org.apache.zookeeper.server.embedded;

/**
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

import com.google.common.collect.ImmutableList;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.yetus.audience.InterfaceAudience;
import org.apache.yetus.audience.InterfaceStability;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.client.ZooKeeperBuilder;
import org.apache.zookeeper.server.ServerCnxnFactory;
import org.apache.zookeeper.server.ZooKeeperServer;
import org.apache.zookeeper.server.embedded.ZKBridgeServerEmbedded.ZKBridgeServerEmbeddedBuilder;
import org.apache.zookeeper.server.embedded.spiral.InMemoryFS;
import org.apache.zookeeper.server.embedded.spiral.InMemorySpiralClient;
import org.apache.zookeeper.server.embedded.spiral.SpiralClientStrategy;
import org.apache.zookeeper.server.embedded.spiral.SpiralClientStrategy.InMemorySpiralClientStrategy;
import org.apache.zookeeper.spiral.SpiralClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This API allows you to start a ZKBridge cluster from Java code <p>
 * The servers will run inside the same JVM process.<p>
 * Typical usecases are:
 * <ul>
 * <li>Running automated tests</li>
 * <li>Launch ZKBridge servers with a Java based service management system</li>
 * </ul>
 * <p>
 * Please take into consideration that in production usually it is better to not run the client
 * together with the server in order to avoid race conditions, especially around how ephemeral nodes work.
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public class ZKBridgeClusterEmbedded implements AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(ZKBridgeClusterEmbedded.class);
    private static final Integer SESSION_TIMEOUT_MS = 10_000; // 10 sec
    private static final AtomicInteger CLIENT_PORT_GENERATOR = new AtomicInteger(2181);
    private static final AtomicInteger ADMIN_SERVER_PORT_GENERATOR = new AtomicInteger(8080);

    public final Integer sessionTimeoutMs;
    public final List<Integer> clientPorts;
    public final List<Integer> adminPorts;
    private final InMemoryFS inMemoryFS;
    private final SpiralClient spiralClient;
    private final List<ServerCnxnFactory> servers;
    private static ZKBridgeServerEmbeddedBuilder builder = null;

    public ZooKeeper[] zkClients;

    public ZKBridgeClusterEmbedded(
        InMemoryFS inMemoryFS, Integer sessionTimeoutMs, List<ServerCnxnFactory> servers,
        List<Integer> clientPorts, List<Integer> adminPorts) {
        this.inMemoryFS = inMemoryFS;
        this.spiralClient = new InMemorySpiralClient(inMemoryFS);
        this.sessionTimeoutMs = sessionTimeoutMs == null ? SESSION_TIMEOUT_MS : sessionTimeoutMs;
        this.servers = servers;
        this.clientPorts = clientPorts;
        this.adminPorts = adminPorts;
        this.zkClients = new ZooKeeper[servers.size()];
    }

    /**
     * Builder for ZooKeeperServerEmbedded.
     */
    public static class ZKBridgeClusterEmbeddedBuilder {

        private Integer numServers;
        private Integer sessionTimeoutMs;
        private SpiralClientStrategy spiralClientStrategy = new InMemorySpiralClientStrategy();
        private List<Integer> clientPorts;
        private List<Integer> adminPorts;

        public ZKBridgeClusterEmbeddedBuilder setNumServers(Integer numServers) {
            this.numServers = numServers;
            return this;
        }

        public ZKBridgeClusterEmbeddedBuilder setSpiralClientStrategy(SpiralClientStrategy spiralClientStrategy) {
            this.spiralClientStrategy = spiralClientStrategy;
            return this;
        }

        public ZKBridgeClusterEmbeddedBuilder setSessionTimeoutMs(Integer sessionTimeoutMs) {
            this.sessionTimeoutMs = sessionTimeoutMs;
            return this;
        }

        public ZKBridgeClusterEmbeddedBuilder setClientPorts(List<Integer> clientPorts) {
            this.clientPorts = clientPorts;
            return this;
        }

        public ZKBridgeClusterEmbeddedBuilder setAdminPorts(List<Integer> adminPorts) {
            this.adminPorts = adminPorts;
            return this;
        }

        /**
         * Validate the configuration and create the server, without starting it.
         * @return the new server
         * @throws Exception
         * @see #start()
         */
        public ZKBridgeClusterEmbedded build() throws Exception {
            if (numServers <= 0) {
                throw new IllegalStateException("number of servers should be more than 1");
            }

            List<ServerCnxnFactory> servers = new ArrayList<>();

            InMemoryFS inMemoryFS = new InMemoryFS();
            if (spiralClientStrategy instanceof InMemorySpiralClientStrategy) {
                InMemorySpiralClientStrategy inMemStrategy = (InMemorySpiralClientStrategy) spiralClientStrategy;
                inMemStrategy.inMemoryFS(inMemoryFS);
            }

            if (ZKBridgeClusterEmbedded.builder == null) {
                ZKBridgeClusterEmbedded.builder = new ZKBridgeServerEmbeddedBuilder();
            }

            for (int idx = 0; idx < numServers; idx ++) {
                
                servers.add(ZKBridgeClusterEmbedded.builder
                    .setServerId(Long.valueOf(idx))
                    .setSpiralClientStrategy(spiralClientStrategy)
                    .setClientPort(clientPorts.get(idx))
                    .setAdminServerPort(adminPorts.get(idx))
                    .setSnapLeaderId(0)
                    .buildAndStart());
            }

            return new ZKBridgeClusterEmbedded(inMemoryFS, sessionTimeoutMs, servers, clientPorts, adminPorts);
        }
    }

    public static void setBuilder(ZKBridgeServerEmbeddedBuilder customZkbServerEmbeddedBuilder) {
        ZKBridgeClusterEmbedded.builder = customZkbServerEmbeddedBuilder;
    }

    public InMemoryFS getInMemoryFS() {
        return inMemoryFS;
    }

    public List<ServerCnxnFactory> getServers() {
        return servers;
    }

    public SpiralClient getSpiralClient() {
        return spiralClient;
    }

    public String getConnectionString(int serverId) {
        if (serverId > servers.size()) {
            throw new RuntimeException("serverId out of range. Cannot provide connection string for id: " + serverId);
        }
        return buildConnectionString(adminPorts.get(serverId));
    }

    private static String buildConnectionString(int serverId) {
        return String.format("localhost:%s", serverId);
    }

    public ZooKeeper getOrBuildClient(int serverId) {
        return getOrBuildClient(serverId, null, new RecordingWatcher());
    }

    public ZooKeeper getOrBuildClient(int serverId, int[] failoverServerIds) {
        return getOrBuildClient(serverId, failoverServerIds, new RecordingWatcher());
    }

    public ZooKeeper getOrBuildClient(int serverId, int[] failoverServerIds, Watcher watcher) {
        try {
            if (zkClients[serverId] != null) {
                zkClients[serverId].close();
            }

            StringBuilder connectionStringBuilder = new StringBuilder();
            // first server is the primary server connection string.
            connectionStringBuilder.append(buildConnectionString(adminPorts.get(serverId)));

            if (failoverServerIds != null) {
                for (int fsId: failoverServerIds) {
                    connectionStringBuilder
                        .append(",")
                        .append(buildConnectionString(adminPorts.get(fsId)));
                }
            }

            return zkClients[serverId] = new ZooKeeperBuilder(connectionStringBuilder.toString(), sessionTimeoutMs)
                .withDefaultWatcher(watcher)
                .build();
        } catch (Exception e) {
            throw new RuntimeException("error while getting or building the ZKB client", e);
        }
    }

    public void restartServer(int serverId) throws Exception {
        ServerCnxnFactory existingSCF = servers.get(serverId);
        ZooKeeperServer existingZks = existingSCF.getZooKeeperServer();
        SpiralClient existingSpiralClient = existingZks.getSpiralClient();
        Path baseDirPath = new File(existingZks.getConf().getDataLogDir()).getParentFile().getParentFile().toPath();

        // shutdown existing ZKS
        existingSCF.shutdown();

        // build and set a new ZKS
        ServerCnxnFactory newSCF = ZKBridgeServerEmbedded.builder()
            .setServerId(Long.valueOf(serverId))
            .setSpiralClientStrategy(SpiralClientStrategy.Builder.passThrough().setSpiralClient(existingSpiralClient))
            .setBaseDir(baseDirPath)
            .setClientPort(clientPorts.get(serverId))
            .setAdminServerPort(adminPorts.get(serverId))
            .setSnapLeaderId(0)
            .buildAndStart();

        servers.set(serverId, newSCF);
    }

    public void shutdownServer(int serverId) throws Exception {
        ServerCnxnFactory existingSCF = servers.get(serverId);
        ZooKeeperServer zks = existingSCF.getZooKeeperServer();
        servers.set(serverId, null);

        // shutdown existing ZKS
        existingSCF.shutdown();
        while (zks.isRunning()) {
            Thread.sleep(1_00);
        }
    }

    public void startServer(int serverId) throws Exception {
        if (servers.get(serverId) != null) {
            // there is already an active server in running mode.
            return;
        }

        // existing cluster level spiral client
        SpiralClient existingSpiralClient = new InMemorySpiralClient(inMemoryFS);

        ServerCnxnFactory newSCF = ZKBridgeServerEmbedded.builder()
            .setServerId(Long.valueOf(serverId))
            .setSpiralClientStrategy(SpiralClientStrategy.Builder.passThrough().setSpiralClient(existingSpiralClient))
            .setClientPort(clientPorts.get(serverId))
            .setAdminServerPort(adminPorts.get(serverId))
            .setSnapLeaderId(0)
            .buildAndStart();

        servers.set(serverId, newSCF);
    }

    // Waits until all other servers are synced with the provided server's LastProcessedZxid or until timeout.
    public boolean waitAllServersSynced(int serverId) {
        int timeout = 10_000;
        long start = System.currentTimeMillis();
        while (true) {
            boolean allSynced = true;
            for (int i = 0; i < servers.size(); i++) {
                if (i == serverId) {
                    continue;
                }
                ServerCnxnFactory scf = servers.get(i);
                if (scf == null) {
                    continue;
                }
                ZooKeeperServer zks = scf.getZooKeeperServer();
                if (zks.getLastProcessedZxid() < servers.get(serverId).getZooKeeperServer().getLastProcessedZxid()) {
                    allSynced = false;
                    break;
                }
            }
            if (allSynced) {
                return true;
            }
            if (System.currentTimeMillis() - start > timeout) {
                LOG.info("Timeout waiting for all servers to sync");
                return false;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException("Interrupted while waiting for all servers to sync", e);
            }
        }
    }

    /**
     * Start the servers in the cluster.
     * @throws Exception
     */
    public void start() throws Exception {
        // embedded servers are spawned with started state. No-op here.
    }

    /**
     * Shutdown gracefully all servers and wait for resources to be released and also
     * closes clients
     */
    @Override
    public void close() {
        for (ServerCnxnFactory server: servers) {
            if (server != null) {
                server.shutdown();
            }
        }
        for (ZooKeeper zk: zkClients) {
            if (zk != null) {
                try {
                    zk.close();
                } catch (InterruptedException e) {
                    throw new RuntimeException("Interrupted while closing the ZKB client", e);
                }
            }
        }
    }

}
