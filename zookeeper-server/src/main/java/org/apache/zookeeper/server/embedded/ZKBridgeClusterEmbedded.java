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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.yetus.audience.InterfaceAudience;
import org.apache.yetus.audience.InterfaceStability;
import org.apache.zookeeper.server.ServerCnxnFactory;
import org.apache.zookeeper.server.embedded.spiral.InMemoryFS;
import org.apache.zookeeper.server.embedded.spiral.SpiralClientStrategy;
import org.apache.zookeeper.server.embedded.spiral.SpiralClientStrategy.InMemorySpiralClientStrategy;
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
    private static final AtomicInteger CLIENT_PORT_GENERATOR = new AtomicInteger(2181);
    private static final AtomicInteger ADMIN_SERVER_PORT_GENERATOR = new AtomicInteger(8080);
    private final List<ServerCnxnFactory> servers;

    public ZKBridgeClusterEmbedded(List<ServerCnxnFactory> servers) {
        this.servers = servers;
    }

    /**
     * Builder for ZooKeeperServerEmbedded.
     */
    public static class ZKBridgeClusterEmbeddedBuilder {

        private Integer numServers;
        private SpiralClientStrategy spiralClientStrategy = new InMemorySpiralClientStrategy();

        public ZKBridgeClusterEmbeddedBuilder setNumServers(Integer numServers) {
            this.numServers = numServers;
            return this;
        }

        public ZKBridgeClusterEmbeddedBuilder setSpiralClientStrategy(SpiralClientStrategy spiralClientStrategy) {
            this.spiralClientStrategy = spiralClientStrategy;
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

            for (int idx = 0; idx < numServers; idx ++) {
                servers.add(new ZKBridgeServerEmbedded.ZKBridgeServerEmbeddedBuilder()
                    .setServerId(Long.valueOf(idx))
                    .setSpiralClientStrategy(spiralClientStrategy)
                    .setClientPort(CLIENT_PORT_GENERATOR.getAndIncrement())
                    .setAdminServerPort(ADMIN_SERVER_PORT_GENERATOR.getAndIncrement())
                    .setSnapLeaderEnabled(idx == 0)
                    .buildAndStart());
            }
            return new ZKBridgeClusterEmbedded(servers);
        }
    }

    static ZKBridgeClusterEmbeddedBuilder builder() {
        return new ZKBridgeClusterEmbeddedBuilder();
    }

    /**
     * Start the servers in the cluster.
     * @throws Exception
     */
    public void start() throws Exception {
        // embedded servers are spawned with started state. No-op here.
    }

    /**
     * Shutdown gracefully the servers and wait for resources to be released.
     */
    @Override
    public void close() {
        for (ServerCnxnFactory server: servers) {
            server.shutdown();
        }
    }

}
