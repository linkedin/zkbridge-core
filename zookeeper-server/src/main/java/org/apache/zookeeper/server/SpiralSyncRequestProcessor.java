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

import java.io.Flushable;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.apache.zookeeper.common.Time;
import org.apache.zookeeper.spiral.SpiralClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This RequestProcessor logs requests to the Spiral Changelog. The request is not
 * passed to the next RequestProcessor until its log has been synced to disk.
 */
public class SpiralSyncRequestProcessor extends ZooKeeperCriticalThread implements RequestProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(SpiralSyncRequestProcessor.class);

    private static final Request REQUEST_OF_DEATH = Request.requestOfDeath;


    private final BlockingQueue<Request> queuedRequests = new LinkedBlockingQueue<>();

    private final SpiralClient spiralClient;
    private final ZooKeeperServer zks;
    private final RequestProcessor nextProcessor;
    private final SpiralTxnLogSyncer spiralTxnLogSyncer;

    public SpiralSyncRequestProcessor(ZooKeeperServer zks, SpiralClient spiralClient, SpiralTxnLogSyncer spiralTxnLogSyncer, RequestProcessor nextProcessor) {
        super("SpiralSyncThread:" + zks.getServerId(), zks.getZooKeeperServerListener());
        this.zks = zks;
        this.spiralTxnLogSyncer = spiralTxnLogSyncer;
        this.spiralClient = spiralClient;
        this.nextProcessor = nextProcessor;
    }

    @Override
    public void run() {
        try {
            while (true) {
                ServerMetrics.getMetrics().SPIRAL_SYNC_PROCESSOR_QUEUE_SIZE.add(queuedRequests.size());
                Request si = queuedRequests.poll(zks.getMaxWriteQueuePollTime(), TimeUnit.MILLISECONDS);
                if (si == null) {
                    si = queuedRequests.take();
                }

                if (si == REQUEST_OF_DEATH) {
                    break;
                }

                long startProcessTime = Time.currentElapsedTime();
                ServerMetrics.getMetrics().SPIRAL_SYNC_PROCESSOR_QUEUE_TIME.add(startProcessTime - si.syncQueueStartTime);
                if (si.getHdr() == null) {
                    // optimization for read heavy workloads
                    // iff this is a read or a throttled request(which doesn't need to be written to the disk),
                    // and there are no pending flushes (writes), then just pass this to the next processor
                    if (nextProcessor != null) {
                        nextProcessor.processRequest(si);
                        if (nextProcessor instanceof Flushable) {
                            ((Flushable) nextProcessor).flush();
                        }
                    }
                    continue;
                }

                // Before applying to shared txn log or internal datatree, the current state of data tree should be atleast upto the current zxid. 
                // And background syncing thread will take care of syncing until latest of shared txn log.
                spiralTxnLogSyncer.syncUptoZxid(si.getHdr().getZxid() - 1);


                // append write requests to the spiral change-log
                zks.getZKDatabase().append(zks.getServerId(), si);

                //TODO: Also we should add snapshot taking code here based on snapCount.

                nextProcessor.processRequest(si);
                ServerMetrics.getMetrics().SPIRAL_SYNC_PROCESS_TIME.add(Time.currentElapsedTime() - startProcessTime);
            }
        } catch (Throwable t) {
            handleException(this.getName(), t);
        }
        LOG.info("SpiralSyncRequestProcessor exited!");
    }

    public void shutdown() {
        LOG.info("Shutting down");
        queuedRequests.add(REQUEST_OF_DEATH);
        try {
            this.join();
        } catch (InterruptedException e) {
            LOG.warn("Interrupted while wating for {} to finish", this);
            Thread.currentThread().interrupt();
        }
        if (nextProcessor != null) {
            nextProcessor.shutdown();
        }
    }

    public void processRequest(final Request request) {
        Objects.requireNonNull(request, "Request cannot be null");

        request.syncQueueStartTime = Time.currentElapsedTime();
        queuedRequests.add(request);
        ServerMetrics.getMetrics().SYNC_PROCESSOR_QUEUED.add(1);
    }

}
