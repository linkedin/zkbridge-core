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
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.zookeeper.server;

import static org.apache.zookeeper.test.ClientBase.CONNECTION_TIMEOUT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.management.JMException;

import org.apache.commons.io.FileUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.PortAssignment;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZKBTest;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZKTestCase;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.common.PathUtils;
import org.apache.zookeeper.jmx.ManagedUtil;
import org.apache.zookeeper.metrics.BaseTestMetricsProvider.MetricsProviderCapturingLifecycle;
import org.apache.zookeeper.metrics.BaseTestMetricsProvider.MetricsProviderWithConfiguration;
import org.apache.zookeeper.metrics.BaseTestMetricsProvider.MetricsProviderWithErrorInConfigure;
import org.apache.zookeeper.metrics.BaseTestMetricsProvider.MetricsProviderWithErrorInStart;
import org.apache.zookeeper.metrics.BaseTestMetricsProvider.MetricsProviderWithErrorInStop;
import org.apache.zookeeper.server.admin.AdminServer.AdminServerException;
import org.apache.zookeeper.server.embedded.ZKBridgeServerEmbedded;
import org.apache.zookeeper.server.embedded.spiral.SpiralClientStrategy.InMemorySpiralClientStrategy;
import org.apache.zookeeper.server.persistence.FileTxnSnapLog;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig.ConfigException;
import org.apache.zookeeper.spiral.SpiralClient;
import org.apache.zookeeper.test.ClientBase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test stand-alone server.
 *
 */
@ZKBTest
public class ZKBridgeServerMainTest extends ZKTestCase implements Watcher {

    protected static final Logger LOG = LoggerFactory.getLogger(ZKBridgeServerMainTest.class);

    private CountDownLatch clientConnected = new CountDownLatch(1);

    public static class MainThread extends Thread {

        final TestZKBMain main;
        final ZKBServerConfig config;
        File confFile = null;
        private Path baseDir;
        private Long serverId = 1L;
        private SpiralClient spiralClient;
        private int clientPort;

        private Properties configuration;

        /*
         * There are two ways to construct config file for ZKBserverMain. In few tests
         * ZKBserverConfig is constructed via code and in few tests config file. Since 
         * few tests expects config errors to be thrown, we need to have a way to construct
         * config file with errors in those cases initConfFile() method is used.
         */
        public MainThread(int clientPort) throws IOException, ConfigException {
            this(clientPort, new HashMap<>());
        }
        
        public MainThread(int clientPort, Map<String, String> configs) throws IOException, ConfigException {
            super("Standalone server with clientPort:" + clientPort);
            this.clientPort = clientPort;
            config = buildServerConfig(clientPort, configs);
            spiralClient = (new InMemorySpiralClientStrategy()).buildSpiralClient();
            main = new TestZKBMain();
        }

        public File initConfFile(String configs) throws IOException{
            File tmpDir = ClientBase.createTmpDir();
            confFile = new File(tmpDir, "zoo.cfg");
            FileWriter fwriter = new FileWriter(confFile);
            fwriter.write("tickTime=2000\n");
            fwriter.write("initLimit=10\n");
            fwriter.write("syncLimit=5\n");
            if (configs != null) {
                fwriter.write(configs);
            }

            File dataDir = new File(tmpDir, "data");
            File logDir = new File(dataDir.toString() + "_txnlog");

            String normalizedDataDir = PathUtils.normalizeFileSystemPath(dataDir.toString());
            String normalizedLogDir = PathUtils.normalizeFileSystemPath(logDir.toString());
            fwriter.write("dataDir=" + normalizedDataDir + "\n");
            fwriter.write("dataLogDir=" + normalizedLogDir + "\n");
            fwriter.write("clientPort=" + clientPort + "\n");

            fwriter.flush();
            fwriter.close();
            return tmpDir;
        }

        public ZKBServerConfig buildServerConfig(int clientPort, Map<String, String> configs) throws IOException, ConfigException {
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
            // Best attempt to cleanup any existing files in dirs. Masking exceptions here because tests can run in parallel and may messup things.
            try {
                FileUtils.deleteDirectory(dataDir);
                FileUtils.deleteDirectory(logDir);
            } catch (IOException e) {
                LOG.info("Error creating directories", e);
            }
            configuration = ZKBridgeServerEmbedded.decorateConfiguration(configuration);
            configuration.setProperty("dataDir", dataDir.getAbsolutePath());
            configuration.setProperty("clientPort", String.valueOf(clientPort));
            for (Map.Entry<String, String> entry : configs.entrySet()) {
                configuration.setProperty(entry.getKey(), entry.getValue());
            }

            Path configFile = Files.createTempFile(baseDir, "zookeeper.configuration", ".properties");
            try (OutputStream oo = Files.newOutputStream(configFile)) {
                configuration.store(oo, "Automatically generated at every-boot");
            }

            final ZKBServerConfig config = new ZKBServerConfig();
            config.parse(configFile.toAbsolutePath().toString());
            config.setServerId(serverId);
            config.setClientPortAddress(new InetSocketAddress(clientPort));

            LOG.info("Current configuration is at {}", configFile.toAbsolutePath());
            LOG.info("using baseDir location: {}", baseDir);
            LOG.info("using client port: {}", config.getClientPortAddress().getPort());
            return config;
        }
        
        public void initializeAndRun() throws IOException, AdminServerException {
            try {
                ManagedUtil.registerLog4jMBeans();
            } catch (JMException e) {
                LOG.warn("Unable to register log4j JMX control", e);
            }

            main.runFromConfig(config, spiralClient);
        }

        public void run(){
            try {
                initializeAndRun();
            } catch (IOException | AdminServerException e) {
                LOG.error("Error running ZKBridgeServerMain", e);
            }
        }

        void deleteDirs(File tmpDir) throws IOException {
            delete(tmpDir);
        }

        void delete(File f) throws IOException {
            if (f.isDirectory()) {
                for (File c : f.listFiles()) {
                    delete(c);
                }
            }
            if (!f.delete()) {
                // double check for the file existence
                if (f.exists()) {
                    throw new IOException("Failed to delete file: " + f);
                }
            }
        }

        public void shutdown() throws IOException {
            main.shutdown();
        }

        ServerCnxnFactory getCnxnFactory() {
            return main.getCnxnFactory();
        }

        public ServerCnxnFactory getSecureCnxnFactory(){
            return main.getSecureCnxnFactory();
        }

    }

    public static class TestZKBMain extends ZKBridgeServerMain {

        public void shutdown() {
            super.shutdown();
        }

    }

    /**
     * Test case for https://issues.apache.org/jira/browse/ZOOKEEPER-2247.
     * Test to verify that even after non recoverable error (error while
     * writing transaction log), ZooKeeper is still available.
     */
    @ZKBTest
    @Timeout(value = 30)
    public void testNonRecoverableError() throws Exception {
        ClientBase.setupTestEnv();

        final int CLIENT_PORT = PortAssignment.unique();

        final Map<String, String> configs = new HashMap<>();
        MainThread main = new MainThread(CLIENT_PORT,configs);
        main.start();

        assertTrue(ClientBase.waitForServerUp("127.0.0.1:" + CLIENT_PORT, CONNECTION_TIMEOUT),
                "waiting for server being up");

        ZooKeeper zk = new ZooKeeper("127.0.0.1:" + CLIENT_PORT, ClientBase.CONNECTION_TIMEOUT, this);

        zk.create("/foo1", "foobar".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        assertEquals(new String(zk.getData("/foo1", null, null)), "foobar");

        // inject problem in server
        ZooKeeperServer zooKeeperServer = main.getCnxnFactory().getZooKeeperServer();
        FileTxnSnapLog snapLog = zooKeeperServer.getTxnLogFactory();
        FileTxnSnapLog fileTxnSnapLog = new FileTxnSnapLog(snapLog.getDataLogDir(), snapLog.getSnapDir());
        ZKDatabase newDBWithError = new ZKDatabase(fileTxnSnapLog) {
            @Override
            public boolean append(Long serverId, Request si) throws IOException {
                throw new IOException("Injected IOException");
            }
        };
        zooKeeperServer.setZKDatabase(newDBWithError);

        try {
            // do create operation, so that injected IOException is thrown
            zk.create("/foo2", "foobar".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            fail("IOException is expected as error is injected in transaction log commit funtionality");
        } catch (Exception e) {
            // do nothing
        }
        zk.close();
        assertTrue(ClientBase.waitForServerDown("127.0.0.1:" + CLIENT_PORT, ClientBase.CONNECTION_TIMEOUT),
                "waiting for server down");
        newDBWithError.close();
        main.shutdown();
    }

    // RR:TODO: We need to add more tests around zkbridge node should not be able to start if it fails to connect to spiral service.

    /**
     * Verify the ability to start a standalone server instance.
     */
    @Test
    public void testStandalone() throws Exception {
        ClientBase.setupTestEnv();

        final int CLIENT_PORT = PortAssignment.unique();

        final Map<String, String> configs = new HashMap<>();
        MainThread main = new MainThread(CLIENT_PORT, configs);
        main.start();

        assertTrue(ClientBase.waitForServerUp("127.0.0.1:" + CLIENT_PORT, CONNECTION_TIMEOUT),
                "waiting for server being up");

        clientConnected = new CountDownLatch(1);
        ZooKeeper zk = new ZooKeeper("127.0.0.1:" + CLIENT_PORT, ClientBase.CONNECTION_TIMEOUT, this);
        assertTrue(clientConnected.await(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS), "Failed to establish zkclient connection!");

        zk.create("/foo", "foobar".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        assertEquals(new String(zk.getData("/foo", null, null)), "foobar");
        zk.close();

        main.shutdown();
        main.join();

        assertTrue(ClientBase.waitForServerDown("127.0.0.1:" + CLIENT_PORT, ClientBase.CONNECTION_TIMEOUT),
                "waiting for server down");
    }

    /**
     * Test verifies that the server shouldn't allow minsessiontimeout greater than
     * maxsessiontimeout
     */
    @Test
    public void testWithMinSessionTimeoutGreaterThanMaxSessionTimeout() throws Exception {
        ClientBase.setupTestEnv();

        final int CLIENT_PORT = PortAssignment.unique();
        final int tickTime = 2000;
        final int minSessionTimeout = 20 * tickTime + 1000; // min is higher
        final int maxSessionTimeout = tickTime * 2 - 100; // max is lower
        final Map<String, String> configMap = new HashMap<>();

        final String configs = "maxSessionTimeout="
                                       + maxSessionTimeout
                                       + "\n"
                                       + "minSessionTimeout="
                                       + minSessionTimeout
                                       + "\n";
        MainThread main = new MainThread(CLIENT_PORT, configMap);
        File tmpDir = main.initConfFile(configs);
        String[] args = new String[1];
        args[0] = main.confFile.toString();
        try {
            main.main.initializeAndRun(args);
            fail("Must throw exception as " + "minsessiontimeout > maxsessiontimeout");
        } catch (ConfigException iae) {
            // expected
        }
        main.deleteDirs(tmpDir);
    }

     /**
     * Test verifies that the server shouldn't boot with an invalid metrics provider
     */
    @Test
    public void testInvalidMetricsProvider() throws Exception {
        ClientBase.setupTestEnv();

        final int CLIENT_PORT = PortAssignment.unique();
        final String configs = "metricsProvider.className=BadClass\n";
        final Map<String, String> configMap = new HashMap<>();
        MainThread main = new MainThread(CLIENT_PORT, configMap);
        File tmpDir = main.initConfFile(configs);
        String[] args = new String[1];
        args[0] = main.confFile.toString();
        try {
            main.main.initializeAndRun(args);
            fail("Must throw exception as metrics provider is not " + "well configured");
        } catch (ConfigException iae) {
            // expected
        }
        main.deleteDirs(tmpDir);
    }

    /**
     * Test verifies that the server shouldn't boot with a faulty metrics provider
     */
    @Test
    public void testFaultyMetricsProviderOnStart() throws Exception {
        ClientBase.setupTestEnv();

        final int CLIENT_PORT = PortAssignment.unique();
        final String configs = "metricsProvider.className=" + MetricsProviderWithErrorInStart.class.getName() + "\n";
        final Map<String, String> configMap = new HashMap<>();
        MainThread main = new MainThread(CLIENT_PORT, configMap);
        File tmpDir = main.initConfFile(configs);
        String[] args = new String[1];
        args[0] = main.confFile.toString();
        try {
            main.main.initializeAndRun(args);
            fail("Must throw exception as metrics provider cannot boot");
        } catch (IOException iae) {
            // expected
        }
        main.deleteDirs(tmpDir);
    }

    /**
     * Test verifies that the server shouldn't boot with a faulty metrics provider
     */
    @Test
    public void testFaultyMetricsProviderOnConfigure() throws Exception {
        ClientBase.setupTestEnv();

        final int CLIENT_PORT = PortAssignment.unique();
        final String configs = "metricsProvider.className="
                                       + MetricsProviderWithErrorInConfigure.class.getName()
                                       + "\n";
        final Map<String, String> configMap = new HashMap<>();
        MainThread main = new MainThread(CLIENT_PORT, configMap);
        File tmpDir = main.initConfFile(configs);
        String[] args = new String[1];
        args[0] = main.confFile.toString();
        try {
            main.main.initializeAndRun(args);
            fail("Must throw exception as metrics provider is cannot boot");
        } catch (IOException iae) {
            // expected
        }
        main.deleteDirs(tmpDir);
    }

    /**
     * Test verifies that the server shouldn't be affected but runtime errors on stop()
     */
    @Test
    public void testFaultyMetricsProviderOnStop() throws Exception {
        ClientBase.setupTestEnv();

        final int CLIENT_PORT = PortAssignment.unique();
        MetricsProviderWithErrorInStop.stopCalled.set(false);
        final Map<String, String> configMap = new HashMap<>();
        configMap.put("metricsProvider.className", MetricsProviderWithErrorInStop.class.getName());
        MainThread main = new MainThread(CLIENT_PORT,configMap);
        main.start();

        assertTrue(ClientBase.waitForServerUp("127.0.0.1:" + CLIENT_PORT, CONNECTION_TIMEOUT),
                "waiting for server being up");

        clientConnected = new CountDownLatch(1);
        ZooKeeper zk = new ZooKeeper("127.0.0.1:" + CLIENT_PORT, ClientBase.CONNECTION_TIMEOUT, this);
        assertTrue(clientConnected.await(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS), "Failed to establish zkclient connection!");

        zk.create("/foo", "foobar".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        assertEquals(new String(zk.getData("/foo", null, null)), "foobar");
        zk.close();

        main.shutdown();
        main.join();
        //main.deleteDirs();

        assertTrue(ClientBase.waitForServerDown("127.0.0.1:" + CLIENT_PORT, ClientBase.CONNECTION_TIMEOUT),
                "waiting for server down");
        assertTrue(MetricsProviderWithErrorInStop.stopCalled.get());
    }

    /**
     * Test verifies that configuration is passed to the MetricsProvider.
     */
    @Test
    public void testMetricsProviderConfiguration() throws Exception {
        ClientBase.setupTestEnv();

        final int CLIENT_PORT = PortAssignment.unique();
        MetricsProviderWithConfiguration.httpPort.set(0);
        final Map<String, String> configMap = new HashMap<>();
        configMap.put("metricsProvider.className", MetricsProviderWithConfiguration.class.getName());
        configMap.put("metricsProvider.httpPort", "1234");
        MainThread main = new MainThread(CLIENT_PORT, configMap);
        main.start();

        assertTrue(ClientBase.waitForServerUp("127.0.0.1:" + CLIENT_PORT, CONNECTION_TIMEOUT),
                "waiting for server being up");

        clientConnected = new CountDownLatch(1);
        ZooKeeper zk = new ZooKeeper("127.0.0.1:" + CLIENT_PORT, ClientBase.CONNECTION_TIMEOUT, this);
        assertTrue(clientConnected.await(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS), "Failed to establish zkclient connection!");

        zk.create("/foo", "foobar".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        assertEquals(new String(zk.getData("/foo", null, null)), "foobar");
        zk.close();

        main.shutdown();
        main.join();
        //main.deleteDirs();

        assertTrue(ClientBase.waitForServerDown("127.0.0.1:" + CLIENT_PORT, ClientBase.CONNECTION_TIMEOUT),
                "waiting for server down");
        assertEquals(1234, MetricsProviderWithConfiguration.httpPort.get());
    }

    /**
     * Test verifies that all of the lifecycle methods of the MetricsProvider are called.
     */
    @Test
    public void testMetricsProviderLifecycle() throws Exception {
        ClientBase.setupTestEnv();
        MetricsProviderCapturingLifecycle.reset();

        final int CLIENT_PORT = PortAssignment.unique();
        final Map<String, String> configMap = new HashMap<>();
        configMap.put("metricsProvider.className", MetricsProviderCapturingLifecycle.class.getName());
        configMap.put("metricsProvider.httpPort", "1234");
        MainThread main = new MainThread(CLIENT_PORT, configMap);
        main.start();

        assertTrue(ClientBase.waitForServerUp("127.0.0.1:" + CLIENT_PORT, CONNECTION_TIMEOUT),
                "waiting for server being up");

        clientConnected = new CountDownLatch(1);
        ZooKeeper zk = new ZooKeeper("127.0.0.1:" + CLIENT_PORT, ClientBase.CONNECTION_TIMEOUT, this);
        assertTrue(clientConnected.await(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS), "Failed to establish zkclient connection!");

        zk.create("/foo", "foobar".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        assertEquals(new String(zk.getData("/foo", null, null)), "foobar");
        zk.close();

        main.shutdown();
        main.join();
        //main.deleteDirs();

        assertTrue(ClientBase.waitForServerDown("127.0.0.1:" + CLIENT_PORT, ClientBase.CONNECTION_TIMEOUT), "waiting for server down");
        assertTrue(MetricsProviderCapturingLifecycle.configureCalled.get(), "metrics provider lifecycle error");
        assertTrue(MetricsProviderCapturingLifecycle.startCalled.get(), "metrics provider lifecycle error");
        assertTrue(MetricsProviderCapturingLifecycle.getRootContextCalled.get(), "metrics provider lifecycle error");
        assertTrue(MetricsProviderCapturingLifecycle.stopCalled.get(), "metrics provider lifecycle error");
    }

    /**
     * Test verifies that the server is able to redefine if user configured only
     * minSessionTimeout limit
     */
    @Test
    public void testWithOnlyMinSessionTimeout() throws Exception {
        ClientBase.setupTestEnv();

        final int CLIENT_PORT = PortAssignment.unique();
        final int tickTime = 2000;
        final int minSessionTimeout = tickTime * 2 - 100;
        int maxSessionTimeout = 20 * tickTime;
        final Map<String, String> configMap = new HashMap<>();
        configMap.put("minSessionTimeout", String.valueOf(minSessionTimeout));
        MainThread main = new MainThread(CLIENT_PORT, configMap);
        main.start();

        String HOSTPORT = "127.0.0.1:" + CLIENT_PORT;
        assertTrue(ClientBase.waitForServerUp(HOSTPORT, CONNECTION_TIMEOUT), "waiting for server being up");
        // create session with min value
        verifySessionTimeOut(minSessionTimeout, minSessionTimeout, HOSTPORT);
        verifySessionTimeOut(minSessionTimeout - 2000, minSessionTimeout, HOSTPORT);
        // create session with max value
        verifySessionTimeOut(maxSessionTimeout, maxSessionTimeout, HOSTPORT);
        verifySessionTimeOut(maxSessionTimeout + 2000, maxSessionTimeout, HOSTPORT);
        main.shutdown();
        assertTrue(ClientBase.waitForServerDown(HOSTPORT, ClientBase.CONNECTION_TIMEOUT), "waiting for server down");
    }

    /**
     * Test verifies that the server is able to redefine the min/max session
     * timeouts
     */
    @Test
    public void testMinMaxSessionTimeOut() throws Exception {
        ClientBase.setupTestEnv();

        final int CLIENT_PORT = PortAssignment.unique();
        final int tickTime = 2000;
        final int minSessionTimeout = tickTime * 2 - 100;
        final int maxSessionTimeout = 20 * tickTime + 1000;
        final Map<String, String> configMap = new HashMap<>();
        configMap.put("maxSessionTimeout", String.valueOf(maxSessionTimeout));
        configMap.put("minSessionTimeout", String.valueOf(minSessionTimeout));
        MainThread main = new MainThread(CLIENT_PORT, configMap);
        main.start();

        String HOSTPORT = "127.0.0.1:" + CLIENT_PORT;
        assertTrue(ClientBase.waitForServerUp(HOSTPORT, CONNECTION_TIMEOUT), "waiting for server being up");
        // create session with min value
        verifySessionTimeOut(minSessionTimeout, minSessionTimeout, HOSTPORT);
        verifySessionTimeOut(minSessionTimeout - 2000, minSessionTimeout, HOSTPORT);
        // create session with max value
        verifySessionTimeOut(maxSessionTimeout, maxSessionTimeout, HOSTPORT);
        verifySessionTimeOut(maxSessionTimeout + 2000, maxSessionTimeout, HOSTPORT);
        main.shutdown();

        assertTrue(ClientBase.waitForServerDown(HOSTPORT, ClientBase.CONNECTION_TIMEOUT), "waiting for server down");
    }

    private void verifySessionTimeOut(int sessionTimeout, int expectedSessionTimeout, String HOSTPORT) throws IOException, KeeperException, InterruptedException {
        clientConnected = new CountDownLatch(1);
        ZooKeeper zk = new ZooKeeper(HOSTPORT, sessionTimeout, this);
        assertTrue(clientConnected.await(sessionTimeout, TimeUnit.MILLISECONDS), "Failed to establish zkclient connection!");
        assertEquals(expectedSessionTimeout, zk.getSessionTimeout(), "Not able to configure the sessionTimeout values");
        zk.close();
    }

    @Test
    public void testJMXRegistrationWithNIO() throws Exception {
        ClientBase.setupTestEnv();
        ServerCnxnFactory server_1 = startServer();
        ServerCnxnFactory server_2 = startServer();

        server_1.shutdown();
        server_2.shutdown();
    }

    @Test
    public void testJMXRegistrationWithNetty() throws Exception {
        String originalServerCnxnFactory = System.getProperty(ServerCnxnFactory.ZOOKEEPER_SERVER_CNXN_FACTORY);
        System.setProperty(ServerCnxnFactory.ZOOKEEPER_SERVER_CNXN_FACTORY, NettyServerCnxnFactory.class.getName());
        try {
            ClientBase.setupTestEnv();
            ServerCnxnFactory server_1 = startServer();
            ServerCnxnFactory server_2 = startServer();

            server_1.shutdown();
            server_2.shutdown();
        } finally {
            // setting back
            if (originalServerCnxnFactory == null || originalServerCnxnFactory.isEmpty()) {
                System.clearProperty(ServerCnxnFactory.ZOOKEEPER_SERVER_CNXN_FACTORY);
            } else {
                System.setProperty(ServerCnxnFactory.ZOOKEEPER_SERVER_CNXN_FACTORY, originalServerCnxnFactory);
            }
        }
    }

    private ServerCnxnFactory startServer() throws Exception {
        final int CLIENT_PORT = PortAssignment.unique();
        ZooKeeperServer zks = getZooKeeperServer(true);
        ServerCnxnFactory f = ServerCnxnFactory.createFactory(CLIENT_PORT, -1);
        f.startup(zks);
        assertNotNull(zks.jmxServerBean, "JMX initialization failed!");
        assertTrue(ClientBase.waitForServerUp("127.0.0.1:" + CLIENT_PORT, CONNECTION_TIMEOUT),
                "waiting for server being up");
        return f;
    }
    
    public void process(WatchedEvent event) {
        if (event.getState() == KeeperState.SyncConnected) {
            clientConnected.countDown();
        }
    }
}
