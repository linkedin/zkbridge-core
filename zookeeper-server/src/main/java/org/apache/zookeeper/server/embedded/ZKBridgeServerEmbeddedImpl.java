package org.apache.zookeeper.server.embedded;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.security.sasl.SaslException;
import org.apache.zookeeper.server.DatadirCleanupManager;
import org.apache.zookeeper.server.ExitCode;
import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZKBServerConfig;
import org.apache.zookeeper.server.ZKBridgeServerMain;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.quorum.QuorumPeer;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
import org.apache.zookeeper.server.quorum.QuorumPeerMain;
import org.apache.zookeeper.spiral.SpiralClient;
import org.apache.zookeeper.util.ServiceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE file distributed with this work for additional information regarding copyright
 * ownership. The ASF licenses this file to you under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the
 * License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the License.
 */

/**
 * Implementation of ZooKeeperServerEmbedded.
 */
class ZKBridgeServerEmbeddedImpl implements ZKBridgeServerEmbedded {

    private static final Logger LOG = LoggerFactory.getLogger(ZKBridgeServerEmbeddedImpl.class);

    private final ZKBServerConfig config;
    private final SpiralClient spiralClient;
    private ZKBridgeServerMain zkBridgeServerMain;
    private Thread thread;
    private final ExitHandler exitHandler;
    private volatile boolean stopping;

    private int boundClientPort;
    private int boundSecureClientPort;

    ZKBridgeServerEmbeddedImpl(Properties p, Path baseDir, SpiralClient spiralClient, ExitHandler exitHandler) throws Exception {
        this.spiralClient = spiralClient;

        if (!p.containsKey("dataDir")) {
            p.put("dataDir", baseDir.resolve("data").toAbsolutePath().toString());
        }
        Path configFile = Files.createTempFile(baseDir, "zookeeper.configuration", ".properties");
        try (OutputStream oo = Files.newOutputStream(configFile)) {
            p.store(oo, "Automatically generated at every-boot");
        }
        this.exitHandler = exitHandler;
        LOG.info("Current configuration is at {}", configFile.toAbsolutePath());
        config = new ZKBServerConfig();
        config.parse(configFile.toAbsolutePath().toString());
        LOG.info("ServerID:" + config.getServerId());
        LOG.info("DataDir:" + config.getDataDir());
        LOG.info("MetricsProviderClassName:" + config.getMetricsProviderClassName());
    }

    @Override
    public void start() throws Exception {
        start(Integer.MAX_VALUE);
    }

    @Override
    public void start(long startupTimeout) throws Exception {
        switch (exitHandler) {
            case EXIT:
                ServiceUtils.setSystemExitProcedure(ServiceUtils.SYSTEM_EXIT);
                break;
            case LOG_ONLY:
                ServiceUtils.setSystemExitProcedure(ServiceUtils.LOG_ONLY);
                break;
            default:
                ServiceUtils.setSystemExitProcedure(ServiceUtils.SYSTEM_EXIT);
                break;
        }
        final CompletableFuture<String> started = new CompletableFuture<>();

        zkBridgeServerMain = new ZKBridgeServerMain() {
            @Override
            public void serverStarted() {
                LOG.info("ZKBridge Server started");
                boundClientPort = getClientPort();
                boundSecureClientPort = getSecureClientPort();
                started.complete(null);
            }
        };

        thread = new Thread("zkbridge-server-main-runner") {
            @Override
            public void run() {
                try {
                    LOG.info("ZK server starting");
                    zkBridgeServerMain.runFromConfig(config);

                    LOG.info("ZK server died. Requesting stop on JVM");
                    if (!stopping) {
                        ServiceUtils.requestSystemExit(ExitCode.EXECUTION_FINISHED.getValue());
                    }
                } catch (Throwable t) {
                    LOG.error("error during server lifecycle", t);
                    zkBridgeServerMain.close();
                    if (!stopping) {
                        ServiceUtils.requestSystemExit(ExitCode.INVALID_INVOCATION.getValue());
                    }
                }
            }
        };
        thread.start();

        try {
            started.get(startupTimeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException err) {
            LOG.info("Startup timed out, trying to close");
            close();
            throw err;
        }
    }

    @Override
    public String getConnectionString() {
        return prettifyConnectionString(config.getClientPortAddress(), boundClientPort);
    }

    @Override
    public String getSecureConnectionString() {
        return prettifyConnectionString(config.getSecureClientPortAddress(), boundSecureClientPort);
    }

    private String prettifyConnectionString(InetSocketAddress confAddress, int boundPort) {
        if (confAddress != null) {
            return confAddress.getHostString()
                .replace("0.0.0.0", "localhost")
                .replace("0:0:0:0:0:0:0:0", "localhost")
                + ":" + boundPort;
        }
        throw new IllegalStateException("No client address is configured");
    }

    @Override
    public void close() {
        LOG.info("Stopping ZKBridge Server");
        stopping = true;
        if (zkBridgeServerMain != null) {
            zkBridgeServerMain.close();
        }
    }
}
