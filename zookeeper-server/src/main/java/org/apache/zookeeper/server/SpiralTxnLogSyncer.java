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

import java.io.IOException;
import org.apache.zookeeper.common.Time;
import org.apache.zookeeper.server.util.SerializeUtils;
import org.apache.zookeeper.spiral.SpiralBucket;
import org.apache.zookeeper.spiral.SpiralClient;
import org.apache.zookeeper.txn.ServerAwareTxnHeader;
import org.apache.zookeeper.txn.TxnHeader;
import org.apache.zookeeper.util.MappingUtils;
import org.apache.zookeeper.util.ServiceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.zookeeper.spiral.InternalStateKey.*;
import static org.apache.zookeeper.spiral.SpiralBucket.*;


/**
 * This is a background Processor that processes the until its until a particular offset, txns are synced to Spiral.
 */
public class SpiralTxnLogSyncer extends ZooKeeperCriticalThread {

    private static final Logger LOG = LoggerFactory.getLogger(SpiralTxnLogSyncer.class);

    private final ZooKeeperServer zks;
    private final Long serverId;
    private final SpiralClient spiralClient;
    private volatile boolean stopping;
    private volatile boolean killed;
    private static int shutdownTimeout = 10000;

    public SpiralTxnLogSyncer(ZooKeeperServer zks, SpiralClient spiralClient) {
        super("SpiralTxnLogSyncer: " + zks.getServerId(), zks.getZooKeeperServerListener());
        this.zks = zks;
        this.serverId = zks.getServerId();
        this.spiralClient = spiralClient;
    }

    @Override
    public void run() {
        while (true) {
            if (killed || stopping) {
                break;
            }
            syncDeltaUntilLatest();
            sleepQuietly(1000);
        }
        LOG.info("SpiralTxnLogSyncer shutdown. Stopped processing requests!ø");
    }

    public synchronized void syncDeltaUntilLatest() {
        try {
            long startProcessTime = Time.currentElapsedTime();
            if (spiralClient.containsKey(INTERNAL_STATE.getBucketName(), LATEST_TRANSACTION_ID.name())) {
                byte[] lastZxidBuf = spiralClient.get(INTERNAL_STATE.getBucketName(), LATEST_TRANSACTION_ID.name());
                syncUntilZxid(Long.valueOf(new String(lastZxidBuf)));
            }
            ServerMetrics.getMetrics().SPIRAL_BACKGROUND_SYNC_PROCESS_TIME.add(Time.currentElapsedTime() - startProcessTime);
        } catch (Throwable t) {
            LOG.error("error while bg syncing until latest Txn Id", t);
        }
    }

    /**
     * This function syncs the ZK DB upto the supplied zxid. This processes all the request that have originated from
     * other servers/writers, but does not process transactions from same server. This can be case where a transaction
     * is actively getting processed via the RequestProcessor and we dont want sync processor to process the same
     * transaction.
     */
    private synchronized void syncUntilZxid(long zxid) throws IOException {
        while (zks.getLastProcessedZxid() < zxid) {
            long nextTxnId = zks.getLastProcessedZxid() + 1;
            byte[] txnBuf = spiralClient.get(SpiralBucket.SHARED_TRANSACTION_LOG.getBucketName(), String.valueOf(nextTxnId));
            SpiralTxnLogEntry logEntry = SerializeUtils.deserializeSpiralTxn(txnBuf);

            ServerAwareTxnHeader saTxnHdr = logEntry.getHeader();
            if (Long.valueOf(saTxnHdr.getServerId()) == serverId && nextTxnId >= zks.getLastProcessedZxid()) {
                LOG.info("Cannot sync a future txn {} while ZKS at txn {} created by this server.",
                    nextTxnId, zks.getLastProcessedZxid());
                break;
            }

            LOG.info("Background Syncing current ZKB with txn: {}", nextTxnId);
            TxnHeader txnHeader = MappingUtils.toTxnHeader(saTxnHdr);
            // Don't process session related txns here, as they are processed by the session tracker
            zks.processTxnInDB(txnHeader, logEntry.getTxn(), null);

            // update the last processed offset
            spiralClient.updateLastProcessedTxn(zks.getServerId(), zks.getLastProcessedZxid());
            spiralClient.put(LAST_PROCESSED_OFFSET.getBucketName(),
                String.valueOf(zks.getServerId()), String.valueOf(zks.getLastProcessedZxid()).getBytes());
        }
    }

    private void sleepQuietly(long millis) {
        try {
            this.sleep(millis);
        } catch (InterruptedException e) {
            LOG.error("Unexpected interruption during sleep!", e);
        }
    }

    public void shutdown() {
        // Try to shutdown gracefully
        LOG.info("Shutting down");
        stopping = true;
        try {
            this.join(shutdownTimeout);
        } catch (InterruptedException e) {
            LOG.warn("Interrupted while waiting for {} to finish", this);
        }

        // Forcibly shutdown if necessary in order to ensure request
        // queue is drained.
        killed = true;
        try {
            this.join();
        } catch (InterruptedException e) {
            LOG.warn("Interrupted while waiting for {} to finish", this);
            ServiceUtils.requestSystemExit(ExitCode.UNEXPECTED_ERROR.getValue());
        }
    }
}
