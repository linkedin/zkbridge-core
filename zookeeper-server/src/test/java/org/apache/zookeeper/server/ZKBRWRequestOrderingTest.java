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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.jute.BinaryOutputArchive;
import org.apache.zookeeper.ZKBTest;
import org.apache.zookeeper.ZKTestCase;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooDefs.OpCode;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.proto.CreateRequest;
import org.apache.zookeeper.proto.GetDataRequest;
import org.apache.zookeeper.server.embedded.spiral.SpiralClientStrategy.InMemorySpiralClientStrategy;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig.ConfigException;
import org.apache.zookeeper.spiral.SpiralClient;
import org.apache.zookeeper.test.ClientBase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Following scenarios are tested here:
 *
 * 1. For each session, requests are processed and the client sees its
 *    responses in order.
 * 2. Write requests are processed in zxid order across all sessions.
 *
 * The following are also tested for here, but are specific to this
 * particular implementation. The underlying issue is that watches can be
 * reset while reading the data. For reads/writes on two different sessions
 * on different nodes, or with reads that do not set watches, the reads can
 * happen in any order relative to the writes. For a read in one session that
 * resets a watch that is triggered by a write on another session, however,
 * we need to ensure that there is no race condition
 *
 * 3. The pipeline needs to be drained before a write request can enter.
 * 4. No in-flight write requests while processing a read request.
 */
@ZKBTest
public class ZKBRWRequestOrderingTest extends ZKTestCase {

    protected static final Logger LOG = LoggerFactory.getLogger(ZKBRWRequestOrderingTest.class);

    // The amount of ms each test case should run
    static final int TEST_RUN_TIME_IN_MS = 5000;
    private AtomicInteger processedReadRequests = new AtomicInteger(0);
    private AtomicInteger processedWriteRequests = new AtomicInteger(0);

    boolean stopped;
    TestZKBridgeServer zkb;
    ArrayList<TestClientThread> testClients = new ArrayList<>();
    SpiralClient spiralClient = null;

    public void setUp(int numClientThreads, int writePercent, File tmpDir) throws Exception {
        stopped = false;
        ClientBase.setupTestEnv();
        zkb = new TestZKBridgeServer(tmpDir, tmpDir, 4000);
        zkb.startup();
        for (int i = 0; i < numClientThreads; ++i) {
            TestClientThread client = new TestClientThread(writePercent);
            testClients.add(client);
            client.start();
        }
    }

    public void setUp(
        int numReadOnlyClientThreads,
        int mixWorkloadClientThreads,
        int writePercent,
        File tmpDir) throws Exception {
        stopped = false;
        ClientBase.setupTestEnv();
        zkb = new TestZKBridgeServer(tmpDir, tmpDir, 4000);
        zkb.startup();
        for (int i = 0; i < mixWorkloadClientThreads; ++i) {
            TestClientThread client = new TestClientThread(writePercent);
            testClients.add(client);
            client.start();
        }
        for (int i = 0; i < numReadOnlyClientThreads; ++i) {
            TestClientThread client = new TestClientThread(0);
            testClients.add(client);
            client.start();
        }
    }

    @AfterEach
    public void tearDown() throws Exception {
        LOG.info("tearDown starting");
        stopped = true;

        zkb.shutdown();
        for (TestClientThread client : testClients) {
            client.interrupt();
            client.join();
        }
        processedReadRequests.set(0);
        processedWriteRequests.set(0);
        testClients.clear();
    }

    private class TestClientThread extends Thread {

        long sessionId;
        int cxid;
        int nodeId;
        int writePercent;

        public TestClientThread(int writePercent) {
            sessionId = zkb.getSessionTracker().createSession(5000);
            this.writePercent = writePercent;
        }

        public void sendWriteRequest() throws Exception {
            ByteArrayOutputStream boas = new ByteArrayOutputStream();
            BinaryOutputArchive boa = BinaryOutputArchive.getArchive(boas);
            CreateRequest createReq = new CreateRequest("/session"
                                                        + Long.toHexString(sessionId)
                                                        + "-"
                                                        + (++nodeId), new byte[0], Ids.OPEN_ACL_UNSAFE, 1);
            createReq.serialize(boa, "request");
            ByteBuffer bb = ByteBuffer.wrap(boas.toByteArray());
            Request req = new Request(null, sessionId, ++cxid, OpCode.create, RequestRecord.fromBytes(bb), new ArrayList<Id>());
            zkb.getFirstProcessor().processRequest(req);

        }

        public void sendReadRequest() throws Exception {
            ByteArrayOutputStream boas = new ByteArrayOutputStream();
            BinaryOutputArchive boa = BinaryOutputArchive.getArchive(boas);
            GetDataRequest getDataRequest = new GetDataRequest("/session"
                                                               + Long.toHexString(sessionId)
                                                               + "-"
                                                               + nodeId, false);
            getDataRequest.serialize(boa, "request");
            ByteBuffer bb = ByteBuffer.wrap(boas.toByteArray());
            Request req = new Request(null, sessionId, ++cxid, OpCode.getData, RequestRecord.fromBytes(bb), new ArrayList<Id>());
            zkb.getFirstProcessor().processRequest(req);
        }

        public void run() {
            Random rand = new Random(Thread.currentThread().getId());
            try {
                sendWriteRequest();
                while (!stopped) {
                    if (rand.nextInt(100) < writePercent) {
                        sendWriteRequest();
                    } else {
                        sendReadRequest();
                    }
                    Thread.sleep(5 + rand.nextInt(95));
                }
            } catch (Exception e) {
                LOG.error("Uncaught exception in test: ", e);
            }
        }

    }

    @Test
    public void testReadOnlyWorkload(@TempDir File tmpDir) throws Exception {
        int numClients = 10;
        LOG.info("testReadOnlyWorkload");
        setUp(numClients, 0, tmpDir);
        synchronized (this) {
            wait(TEST_RUN_TIME_IN_MS);
        }
        assertFalse(fail);
        assertTrue(processedReadRequests.get() > 0, "No read requests processed");
        // processedWriteRequests.get() == numClients since each client performs one write at the beginning (creates a znode)
        assertTrue(processedWriteRequests.get() == numClients, "Write requests processed");
    }

    @Test
    public void testMixedWorkload(@TempDir File tmpDir) throws Exception {
        int numClients = 10;
        LOG.info("testMixedWorkload 25w/75r workload test");
        setUp(numClients, 25, tmpDir);
        synchronized (this) {
            wait(TEST_RUN_TIME_IN_MS);
        }
        assertFalse(fail);
        checkProcessedRequest();
    }

    @Test
    public void testReadWriteClientMixedWorkload(@TempDir File tmpDir) throws Exception {
        setUp(8, 8, 25, tmpDir);
        LOG.info("testReadWriteClientMixedWorkload 8X0w/100r + 8X25w/75r workload test");
        synchronized (this) {
            wait(TEST_RUN_TIME_IN_MS);
        }
        assertFalse(fail);
        checkProcessedRequest();
    }

    private void checkProcessedRequest() {
        assertTrue(processedReadRequests.get() > 0, "No read requests processed");
        assertTrue(processedWriteRequests.get() > 0, "No write requests processed");
    }

    volatile boolean fail = false;
    private synchronized void failTest(String reason) {
        fail = true;
        notifyAll();
        fail(reason);
    }

    private class TestZKBridgeServer extends ZooKeeperServer {

        public TestZKBridgeServer(File snapDir, File logDir, int tickTime) throws IOException, ConfigException {
            super(snapDir, logDir, tickTime);
            spiralClient = (new InMemorySpiralClientStrategy()).buildSpiralClient();
            setSpiralClient(spiralClient);
        }

        public PrepRequestProcessor getFirstProcessor() {
            return (PrepRequestProcessor) firstProcessor;
        }

        // Prep -> SpiralSync -> Validate -> Final
        @Override
        protected void setupRequestProcessors() {
            RequestProcessor finalProcessor = new FinalRequestProcessor(this);
            // ValidateProcessor is set up in a similar fashion to ToBeApplied
            // processor, so it can do pre/post validating of requests
            ValidateProcessor validateProcessor = new ValidateProcessor(finalProcessor);
            RequestProcessor syncProcessor;
            SpiralTxnLogSyncer spiralTxnLogSyncer = null;
            spiralTxnLogSyncer = new SpiralTxnLogSyncer(this, spiralClient);
                syncProcessor = new SpiralSyncRequestProcessor(this, spiralClient, spiralTxnLogSyncer, validateProcessor);
                ((SpiralSyncRequestProcessor) syncProcessor).start();
            firstProcessor = new PrepRequestProcessor(this, syncProcessor);
            ((PrepRequestProcessor) firstProcessor).start();

            // Start background syncing from shared txn log stored in spiral.
            spiralTxnLogSyncer.start();
        }

    }

    private class ValidateProcessor implements RequestProcessor {

        Random rand = new Random(Thread.currentThread().getId());
        RequestProcessor nextProcessor;
        AtomicLong expectedZxid = new AtomicLong(0);
        ConcurrentHashMap<Long, AtomicInteger> cxidMap = new ConcurrentHashMap<>();

        AtomicInteger outstandingReadRequests = new AtomicInteger(0);
        AtomicInteger outstandingWriteRequests = new AtomicInteger(0);

        public ValidateProcessor(RequestProcessor nextProcessor) {
            this.nextProcessor = nextProcessor;
        }

        @Override
        public void processRequest(Request request) throws RequestProcessorException {
            if (stopped) {
                return;
            }
            if (request.type == OpCode.closeSession) {
                LOG.info("ValidateProcessor got closeSession request=" + request);
                nextProcessor.processRequest(request);
                return;
            }

            if (request.getHdr() != null) {
                outstandingWriteRequests.incrementAndGet();
                validateWriteRequestVariant(request);
                LOG.info("Starting write request zxid={}", request.zxid);
            } else {
                LOG.info(
                    "Starting read request cxid={} for session 0x{}",
                    request.cxid,
                    Long.toHexString(request.sessionId));
                outstandingReadRequests.incrementAndGet();
                validateReadRequestVariant(request);
            }

            // Insert random delay to test thread race conditions
            try {
                Thread.sleep(5 + rand.nextInt(25));
            } catch (InterruptedException e) {
                // ignore
            }
            nextProcessor.processRequest(request);
            /*
             * The commit workers will have to execute this line before they
             * wake up the commit processor. So this value is up-to-date when
             * variant check is performed
             */
            if (request.getHdr() != null) {
                outstandingWriteRequests.decrementAndGet();
                LOG.info("Done write request zxid={}", request.zxid);
                processedWriteRequests.incrementAndGet();
            } else {
                outstandingReadRequests.decrementAndGet();
                LOG.info(
                    "Done read request cxid={} for session 0x{}",
                    request.cxid,
                    Long.toHexString(request.sessionId));
                processedReadRequests.incrementAndGet();
            }
            validateRequest(request);
        }

        /**
         * Validate that this is the only request in the pipeline
         */
        private void validateWriteRequestVariant(Request request) {
            if (stopped) {
                return;
            }
            long zxid = request.getHdr().getZxid();
            int readRequests = outstandingReadRequests.get();
            if (readRequests != 0) {
                failTest("There are " + readRequests + " outstanding"
                         + " read requests while issuing a write request zxid=" + zxid);
            }
            int writeRequests = outstandingWriteRequests.get();
            if (writeRequests > 1) {
                failTest("There are " + writeRequests + " outstanding"
                         + " write requests while issuing a write request zxid=" + zxid
                         + " (expected one)");
            }
        }

        /**
         * Validate that no write request is in the pipeline while working
         * on a read request
         */
        private void validateReadRequestVariant(Request request) {
            int writeRequests = outstandingWriteRequests.get();
            if (writeRequests != 0) {
                failTest("There are " + writeRequests + " outstanding"
                         + " write requests while issuing a read request cxid=" + request.cxid
                         + " for session 0x" + Long.toHexString(request.sessionId));
            }
        }

        private void validateRequest(Request request) {
            LOG.info("Got request {}", request);

            // Zxids should always be in order for write requests
            if (request.getHdr() != null) {
                long zxid = request.getHdr().getZxid();
                if (!expectedZxid.compareAndSet(zxid, zxid + 1)) {
                    failTest("Write request, expected_zxid=" + expectedZxid.get() + "; req_zxid=" + zxid);
                }
            }

            // Each session should see its cxids in order
            AtomicInteger sessionCxid = cxidMap.get(request.sessionId);
            if (sessionCxid == null) {
                sessionCxid = new AtomicInteger(request.cxid + 1);
                AtomicInteger existingSessionCxid = cxidMap.putIfAbsent(request.sessionId, sessionCxid);
                if (existingSessionCxid != null) {
                    failTest("Race condition adding cxid="
                             + request.cxid
                             + " for session 0x"
                             + Long.toHexString(request.sessionId)
                             + " with other_cxid="
                             + existingSessionCxid.get());
                }
            } else {
                if (!sessionCxid.compareAndSet(request.cxid, request.cxid + 1)) {
                    failTest("Expected_cxid=" + sessionCxid.get() + "; req_cxid=" + request.cxid);
                }
            }
        }

        @Override
        public void shutdown() {
            LOG.info("shutdown validateReadRequestVariant");
            cxidMap.clear();
            expectedZxid = new AtomicLong(1);
            if (nextProcessor != null) {
                nextProcessor.shutdown();
            }
        }

    }

}
