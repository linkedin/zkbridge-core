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

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.apache.yetus.audience.InterfaceAudience;
import org.apache.yetus.audience.InterfaceStability;
import org.apache.zookeeper.common.StringUtils;
import org.apache.zookeeper.server.ZooKeeperServer;
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
    private final List<ZooKeeperServer> servers;
    private final InMemoryFS inMemoryFS;

    public ZKBridgeClusterEmbedded(List<ZooKeeperServer> servers, InMemoryFS inMemoryFS) {
        this.servers = servers;
        this.inMemoryFS = inMemoryFS;
    }

    public InMemoryFS getInMemoryFS() {
        return inMemoryFS;
    }

    /**
     * Builder for ZooKeeperServerEmbedded.
     */
    public static class ZKBridgeClusterEmbeddedBuilder {

        private Integer numServers;
        private InMemoryFS inMemoryFS;
        private String identityCert;
        private String identityKey;
        private String spiralEndpoint;
        private String overrideAuthority;
        private String spiralNamespace;
        private boolean useEmbeddedSpiral = true;

        public ZKBridgeClusterEmbeddedBuilder setNumServers(Integer numServers) {
            this.numServers = numServers;
            return this;
        }

        public ZKBridgeClusterEmbeddedBuilder setIdentityCert(String identityCert) {
            useEmbeddedSpiral = false;
            this.identityCert = identityCert;
            return this;
        }

        public ZKBridgeClusterEmbeddedBuilder setIdentityKey(String identityKey) {
            useEmbeddedSpiral = false;
            this.identityKey = identityKey;
            return this;
        }

        public ZKBridgeClusterEmbeddedBuilder setSpiralEndpoint(String spiralEndpoint) {
            useEmbeddedSpiral = false;
            this.spiralEndpoint = spiralEndpoint;
            return this;
        }

        public ZKBridgeClusterEmbeddedBuilder setOverrideAuthority(String overrideAuthority) {
            useEmbeddedSpiral = false;
            this.overrideAuthority = overrideAuthority;
            return this;
        }

        public ZKBridgeClusterEmbeddedBuilder setSpiralNamespace(String spiralNamespace) {
            this.spiralNamespace = spiralNamespace;
            return this;
        }

        public ZKBridgeClusterEmbeddedBuilder setUseEmbeddedSpiral() {
            this.useEmbeddedSpiral = true;
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

            List<ZooKeeperServer> servers = new ArrayList<>();
            inMemoryFS = new InMemoryFS();
            for (int idx = 0; idx < numServers; idx ++) {
                servers.add(new ZKBridgeServerEmbedded.ZKBridgeServerEmbeddedBuilder()
                    .setServerId(Long.valueOf(idx))
                    .setInMemoryFS(inMemoryFS)
                    .setSpiralEndpoint(spiralEndpoint)
                    .setIdentityCert(identityCert)
                    .setIdentityKey(identityKey)
                    .setOverrideAuthority(overrideAuthority)
                    .setUseEmbeddedSpiral(useEmbeddedSpiral)
                    .setSpiralNamespace(spiralNamespace)
                    .build());
            }

            return new ZKBridgeClusterEmbedded(servers, inMemoryFS);
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
        for (ZooKeeperServer server: servers) {
            server.startup();
        }
    }

    /**
     * Start the servers in the cluster
     * @param startupTimeout time to wait in millis for the server to start
     * @throws Exception
     */
    public void start(long startupTimeout) throws Exception {
        for (ZooKeeperServer server: servers) {
            server.startup();
        }
    }

    /**
     * Shutdown gracefully the servers and wait for resources to be released.
     */
    @Override
    public void close() {
        for (ZooKeeperServer server: servers) {
            server.shutdown(true);
        }
    }

}
