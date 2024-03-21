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

import java.nio.file.Path;
import java.util.Objects;
import java.util.Properties;
import org.apache.yetus.audience.InterfaceAudience;
import org.apache.yetus.audience.InterfaceStability;
import org.apache.zookeeper.spiral.SpiralClient;
import org.apache.zookeeper.spiral.SpiralClientImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This API allows you to start a ZKBridge server node from Java code <p>
 * The server will run inside the same process.<p>
 * Typical usecases are:
 * <ul>
 * <li>Running automated tests</li>
 * <li>Launch ZKBridge server with a Java based service management system</li>
 * </ul>
 * <p>
 * Please take into consideration that in production usually it is better to not run the client
 * together with the server in order to avoid race conditions, especially around how ephemeral nodes work.
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public interface ZKBridgeServerEmbedded extends AutoCloseable {

    Logger LOG = LoggerFactory.getLogger(ZKBridgeServerEmbeddedImpl.class);

    /**
     * Builder for ZooKeeperServerEmbedded.
     */
    class ZKBridgeServerEmbeddedBuilder {

        private Path baseDir;
        private Integer serverId;
        private Properties configuration;
        private String identityCert;
        private String identityKey;
        private String spiralEndpoint;
        private String overrideAuthority;
        private String spiralNamespace;
        private boolean useEmbeddedSpiral = true;
        private String caBundle = "/etc/riddler/ca-bundle.crt";

        public ZKBridgeServerEmbeddedBuilder setIdentityCert(String identityCert) {
            useEmbeddedSpiral = false;
            this.identityCert = identityCert;
            return this;
        }

        public ZKBridgeServerEmbeddedBuilder setIdentityKey(String identityKey) {
            useEmbeddedSpiral = false;
            this.identityKey = identityKey;
            return this;
        }

        public ZKBridgeServerEmbeddedBuilder setSpiralEndpoint(String spiralEndpoint) {
            useEmbeddedSpiral = false;
            this.spiralEndpoint = spiralEndpoint;
            return this;
        }

        public ZKBridgeServerEmbeddedBuilder setOverrideAuthority(String overrideAuthority) {
            useEmbeddedSpiral = false;
            this.overrideAuthority = overrideAuthority;
            return this;
        }

        public ZKBridgeServerEmbeddedBuilder setSpiralNamespace(String spiralNamespace) {
            this.spiralNamespace = spiralNamespace;
            return this;
        }

        public ZKBridgeServerEmbeddedBuilder setUseEmbeddedSpiral(boolean useEmbeddedSpiral) {
            this.useEmbeddedSpiral = useEmbeddedSpiral;
            return this;
        }
        private ExitHandler exitHandler = ExitHandler.EXIT;

        /**
         * Base directory of the server.
         * The system will create a temporary configuration file inside this directory.
         * Please remember that dynamic configuration files wil be saved into this directory by default.
         * <p>
         * If you do not set a 'dataDir' configuration entry the system will use a subdirectory of baseDir.
         * @param baseDir
         * @return the builder
         */
        public ZKBridgeServerEmbeddedBuilder baseDir(Path baseDir) {
            this.baseDir = Objects.requireNonNull(baseDir);
            return this;
        }

        /**
         * Server Id of the ZKBridge Server
         * <p>
         * @param serverId
         * @return the builder
         */
        public ZKBridgeServerEmbeddedBuilder setServerId(Integer serverId) {
            this.serverId = serverId;
            return this;
        }

        /**
         * Set the contents of the main configuration as it would be in zk_server.conf file.
         * @param configuration the configuration
         * @return the builder
         */
        public ZKBridgeServerEmbeddedBuilder configuration(Properties configuration) {
            this.configuration = Objects.requireNonNull(configuration);
            return this;
        }

        /**
         * Set the behaviour in case of hard system errors, see {@link ExitHandler}.
         * @param exitHandler the handler
         * @return the builder
         */
        public ZKBridgeServerEmbeddedBuilder exitHandler(ExitHandler exitHandler) {
            this.exitHandler = Objects.requireNonNull(exitHandler);
            return this;
        }

        /**
         * Validate the configuration and create the server, without starting it.
         * @return the new server
         * @throws Exception
         * @see #start()
         */
        public ZKBridgeServerEmbedded build() throws Exception {
            if (serverId == null) {
                throw new IllegalStateException("serverId is null");
            }

            configuration = decorateConfiguration(configuration);

            SpiralClient spiralClient;
            if (useEmbeddedSpiral) {
                LOG.info("No spiralClient is supplied, will use embedded one.");
                spiralClient = new SpiralEmbeddedClient();
            } else {
                spiralClient = new SpiralClientImpl.SpiralClientBuilder()
                    .setSpiralEndpoint(spiralEndpoint)
                    .setIdentityCert(identityCert)
                    .setIdentityKey(identityKey)
                    .setCaBundle(caBundle)
                    .setOverrideAuthority(overrideAuthority)
                    .setNamespace(spiralNamespace)
                    .build();
            }

            return new ZKBridgeServerEmbeddedImpl(configuration, baseDir, serverId, spiralClient, exitHandler);
        }
    }

    static Properties decorateConfiguration(Properties configuration) {
        Properties outputConfig = (configuration == null) ? new Properties() : configuration;

        if (!outputConfig.containsKey("tickTime")) {
            outputConfig.setProperty("tickTime", String.valueOf(2000));
        }
        if (!outputConfig.containsKey("spiral.enabled")) {
            outputConfig.setProperty("spiral.enabled", String.valueOf(true));
        }
        if (!outputConfig.containsKey("spiral.namespace")) {
            outputConfig.setProperty("spiral.namespace", "zookeeper");
        }
        if (!outputConfig.containsKey("standaloneEnabled")) {
            outputConfig.setProperty("standaloneEnabled", String.valueOf(true));
        }
        if (!outputConfig.containsKey("reconfigEnabled")) {
            outputConfig.setProperty("reconfigEnabled", String.valueOf(false));
        }
        if (!outputConfig.containsKey("initLimit")) {
            outputConfig.setProperty("initLimit", String.valueOf(10));
        }
        if (!outputConfig.containsKey("minSessionTimeout")) {
            outputConfig.setProperty("minSessionTimeout", String.valueOf(-1));
        }
        if (!outputConfig.containsKey("maxSessionTimeout")) {
            outputConfig.setProperty("maxSessionTimeout", String.valueOf(-1));
        }

        return outputConfig;
    }

    static ZKBridgeServerEmbeddedBuilder builder() {
        return new ZKBridgeServerEmbeddedBuilder();
    }

    /**
     * Start the server.
     * @throws Exception
     */
    void start() throws Exception;

    /**
     * Start the server
     * @param startupTimeout time to wait in millis for the server to start
     * @throws Exception
     */
    void start(long startupTimeout) throws Exception;

    /**
     * Get a connection string useful for the client.
     * @return the connection string
     * @throws Exception in case the connection string is not available
     */
    String getConnectionString() throws Exception;

    String getSecureConnectionString() throws Exception;

    /**
     * Shutdown gracefully the server and wait for resources to be released.
     */
    @Override
    void close();

}
