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
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import jdk.internal.util.xml.impl.Pair;
import org.apache.commons.io.FileUtils;
import org.apache.yetus.audience.InterfaceAudience;
import org.apache.yetus.audience.InterfaceStability;
import org.apache.zookeeper.metrics.MetricsProvider;
import org.apache.zookeeper.metrics.MetricsProviderLifeCycleException;
import org.apache.zookeeper.metrics.impl.MetricsProviderBootstrap;
import org.apache.zookeeper.server.ServerMetrics;
import org.apache.zookeeper.server.ServerCnxnFactory;
import org.apache.zookeeper.server.ZKBServerConfig;
import org.apache.zookeeper.server.ZooKeeperServer;
import org.apache.zookeeper.server.auth.ProviderRegistry;
import org.apache.zookeeper.server.embedded.spiral.SpiralClientStrategy;
import org.apache.zookeeper.server.embedded.spiral.SpiralClientStrategy.InMemorySpiralClientStrategy;
import org.apache.zookeeper.server.persistence.FileTxnSnapLog;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
import org.apache.zookeeper.spiral.SpiralClient;
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

    Logger LOG = LoggerFactory.getLogger(ZKBridgeServerEmbedded.class);
    Integer MAX_CONNECTIONS = 10;
    AtomicInteger CLIENT_PORT_GENERATOR = new AtomicInteger(2181);
    AtomicInteger ADMIN_SERVER_PORT_GENERATOR = new AtomicInteger(8080);

    /**
     * Builder for ZooKeeperServerEmbedded.
     */
    class ZKBridgeServerEmbeddedBuilder {

        private Path baseDir;
        private Long serverId;

        private Integer clientPort;
        private Integer adminServerPort;
        private Boolean snapLeaderEnabled = false;
        private SpiralClientStrategy spiralClientStrategy = new InMemorySpiralClientStrategy();
        private Properties configuration;

        /**
         * Base directory of the server.
         * The system will create a temporary configuration file inside this directory.
         * Please remember that dynamic configuration files wil be saved into this directory by default.
         * <p>
         * If you do not set a 'dataDir' configuration entry the system will use a subdirectory of baseDir.
         * @param baseDir
         * @return the builder
         */
        public ZKBridgeServerEmbeddedBuilder setBaseDir(Path baseDir) {
            this.baseDir = Objects.requireNonNull(baseDir);
            return this;
        }

        /**
         * Server Id of the ZKBridge Server
         * <p>
         * @param serverId
         * @return the builder
         */
        public ZKBridgeServerEmbeddedBuilder setServerId(Long serverId) {
            this.serverId = serverId;
            return this;
        }

        public ZKBridgeServerEmbeddedBuilder setClientPort(Integer clientPort) {
            this.clientPort = clientPort;
            return this;
        }

        public ZKBridgeServerEmbeddedBuilder setAdminServerPort(Integer adminServerPort) {
            this.adminServerPort = adminServerPort;
            return this;
        }

        public ZKBridgeServerEmbeddedBuilder setSpiralClientStrategy(SpiralClientStrategy spiralClientStrategy) {
            this.spiralClientStrategy = spiralClientStrategy;
            return this;
        }

        public ZKBridgeServerEmbeddedBuilder setSnapLeaderEnabled(Boolean snapLeaderEnabled) {
            this.snapLeaderEnabled = snapLeaderEnabled;
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
         * Validate the configuration and create the server and starting it.
         * @return the new server
         * @throws Exception
         */
        public ServerCnxnFactory buildAndStart() throws Exception {
            ZooKeeperServer zooKeeperServer = buildServer();

            // start server connection
            LOG.info("creating server instance 127.0.0.1:{}", adminServerPort);
            ServerCnxnFactory factory = ServerCnxnFactory.createFactory(adminServerPort, MAX_CONNECTIONS);
            factory.startup(zooKeeperServer);
            return factory;
        }

        public ZooKeeperServer buildServer() throws Exception {
            if (serverId == null) {
                throw new IllegalStateException("serverId is null");
            }

            if (baseDir == null) {
                String zkDir = String.format("zkb-server-%s", serverId);
                final String baseDirLoc = "/tmp/" + zkDir;
                File baseDirFile = new File(baseDirLoc);
                FileUtils.forceMkdir(baseDirFile);
                baseDir = baseDirFile.toPath();
            }
            final File logDir = new File(baseDir + "/logs");
            final File dataDir = new File(baseDir + "/dataDir");
            FileUtils.deleteDirectory(dataDir);
            FileUtils.deleteDirectory(logDir);

            configuration = decorateConfiguration(configuration);
            configuration.putIfAbsent("dataDir", dataDir.getAbsolutePath());

            Path configFile = Files.createTempFile(baseDir, "zookeeper.configuration", ".properties");
            try (OutputStream oo = Files.newOutputStream(configFile)) {
                configuration.store(oo, "Automatically generated at every-boot");
            }

            final ZKBServerConfig config = new ZKBServerConfig();
            config.parse(configFile.toAbsolutePath().toString());
            config.setServerId(serverId);

            LOG.info("Current configuration is at {}", configFile.toAbsolutePath());
            LOG.info("using baseDir location: {}", baseDir);
            LOG.info("using client port: {}", config.getClientPortAddress().getPort());

            return buildServerFromConfig(config);
        }

        ZooKeeperServer buildServerFromConfig(ZKBServerConfig config) throws Exception {
            MetricsProvider metricsProvider;
            try {
                metricsProvider = MetricsProviderBootstrap.startMetricsProvider(
                    config.getMetricsProviderClassName(),
                    config.getMetricsProviderConfiguration());
            } catch (MetricsProviderLifeCycleException error) {
                throw new IOException("Cannot boot MetricsProvider " + config.getMetricsProviderClassName(), error);
            }

            ServerMetrics.metricsProviderInitialized(metricsProvider);
            ProviderRegistry.initialize();

            FileTxnSnapLog txnLog = new FileTxnSnapLog(config.getDataLogDir(), config.getDataDir());
            ZooKeeperServer zkServer = new ZooKeeperServer(
                txnLog, config.getTickTime(), config.getMinSessionTimeout(), config.getMaxSessionTimeout(),
                config.getClientPortListenBacklog(), null, "", false);

            zkServer.setSpiralClient(spiralClientStrategy.buildSpiralClient());
            zkServer.setServerId(config.getServerId());
            zkServer.setSnapLeaderEnabled(snapLeaderEnabled);

            return zkServer;
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
        if (!outputConfig.containsKey("clientPort")) {
            outputConfig.setProperty("clientPort", String.valueOf(CLIENT_PORT_GENERATOR.getAndIncrement()));
        }
        if (!outputConfig.containsKey("admin.serverPort")) {
            outputConfig.setProperty("admin.serverPort", String.valueOf(ADMIN_SERVER_PORT_GENERATOR.getAndIncrement()));
        }
        return outputConfig;
    }

    static ZKBridgeServerEmbeddedBuilder builder() {
        return new ZKBridgeServerEmbeddedBuilder();
    }

}
