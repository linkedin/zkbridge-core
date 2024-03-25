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
import org.apache.commons.io.FileUtils;
import org.apache.yetus.audience.InterfaceAudience;
import org.apache.yetus.audience.InterfaceStability;
import org.apache.zookeeper.metrics.MetricsProvider;
import org.apache.zookeeper.metrics.MetricsProviderLifeCycleException;
import org.apache.zookeeper.metrics.impl.MetricsProviderBootstrap;
import org.apache.zookeeper.server.ServerMetrics;
import org.apache.zookeeper.server.ZKBServerConfig;
import org.apache.zookeeper.server.ZooKeeperServer;
import org.apache.zookeeper.server.auth.ProviderRegistry;
import org.apache.zookeeper.server.persistence.FileTxnSnapLog;
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

    Logger LOG = LoggerFactory.getLogger(ZKBridgeServerEmbedded.class);

    /**
     * Builder for ZooKeeperServerEmbedded.
     */
    class ZKBridgeServerEmbeddedBuilder {

        private Path baseDir;
        private Long serverId;
        private InMemoryFS inMemoryFS;
        private Properties configuration;
        private String identityCert;
        private String identityKey;
        private String spiralEndpoint;
        private String overrideAuthority;
        private String spiralNamespace;
        private SpiralClient spiralClient;
        private boolean useEmbeddedSpiral = true;
        private String caBundle = "/etc/riddler/ca-bundle.crt";

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

        public ZKBridgeServerEmbeddedBuilder setInMemoryFS(InMemoryFS inMemoryFS) {
            this.inMemoryFS = inMemoryFS;
            return this;
        }

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

        public ZKBridgeServerEmbeddedBuilder setSpiralClient(SpiralClient spiralClient) {
            this.spiralClient = spiralClient;
            return this;
        }

        private ExitHandler exitHandler = ExitHandler.EXIT;

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
         */
        public ZooKeeperServer build() throws Exception {
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

            LOG.info("using baseDir location: {}", baseDir);

            configuration = decorateConfiguration(configuration);
            configuration.putIfAbsent("dataDir", dataDir.getAbsolutePath());

            if (useEmbeddedSpiral) {
                LOG.info("No spiralClient is supplied, will use embedded one.");
                inMemoryFS = inMemoryFS == null ? new InMemoryFS() : inMemoryFS;
                spiralClient = new InMemorySpiralClient(inMemoryFS);
            } else if (spiralClient == null) {
                spiralClient = new SpiralClientImpl.SpiralClientBuilder()
                    .setSpiralEndpoint(spiralEndpoint)
                    .setIdentityCert(identityCert)
                    .setIdentityKey(identityKey)
                    .setCaBundle(caBundle)
                    .setOverrideAuthority(overrideAuthority)
                    .setNamespace(spiralNamespace)
                    .build();
            }

            Path configFile = Files.createTempFile(baseDir, "zookeeper.configuration", ".properties");
            try (OutputStream oo = Files.newOutputStream(configFile)) {
                configuration.store(oo, "Automatically generated at every-boot");
            }

            this.exitHandler = exitHandler;
            LOG.info("Current configuration is at {}", configFile.toAbsolutePath());
            final ZKBServerConfig config = new ZKBServerConfig();
            config.parse(configFile.toAbsolutePath().toString());
            config.setServerId(serverId);
            return buildServerFromConfig(config, spiralClient);
        }
    }

    static ZooKeeperServer buildServerFromConfig(ZKBServerConfig config, SpiralClient spiralClient) throws IOException {
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

        zkServer.setSpiralClient(spiralClient);
        zkServer.setServerId(config.getServerId());
        return zkServer;
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

}
