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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.zookeeper.spiral.InternalStateKey.*;
import static org.apache.zookeeper.spiral.SpiralBucket.*;


/**
 * This is a background Processor that processes the until its until a particular offset, txns are synced to Spiral.
 */
public class SpiralSyncProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(SpiralSyncProcessor.class);


    private final SpiralClient spiralClient;
    private final ZooKeeperServer zks;

    public SpiralSyncProcessor(ZooKeeperServer zks, SpiralClient spiralClient) {
        LOG.info("SpiralSyncProcessThread: {}", zks.getServerId());
        this.zks = zks;
        this.spiralClient = spiralClient;
    }

    public void syncDeltaUntilLatest() {
        try {
            long startProcessTime = Time.currentElapsedTime();
            byte[] lastZxidBuf = spiralClient.get(INTERNAL_STATE.getBucketName(), LATEST_TRANSACTION_ID.name());
            syncUntilZxid(Long.valueOf(new String(lastZxidBuf)));
            ServerMetrics.getMetrics().SPIRAL_BACKGROUND_SYNC_PROCESS_TIME.add(Time.currentElapsedTime() - startProcessTime);
        } catch (Throwable t) {
            LOG.error("error while bg syncing until latest Txn Id", t);
        }
        LOG.info("SpiralSyncRequestProcessor exited!");
    }

    private void syncUntilZxid(long zxid) throws IOException {
        while (zks.getLastProcessedZxid() < zxid - 1) {
            long nextToProcessTxnId = zks.getLastProcessedZxid() + 1;
            LOG.info("Background Syncing current ZKB with txn: {}", nextToProcessTxnId);
            byte[] txnBuf = spiralClient.get(SpiralBucket.SHARED_TRANSACTION_LOG.getBucketName(), String.valueOf(nextToProcessTxnId));
            TxnLogEntry logEntry = SerializeUtils.deserializeTxn(txnBuf);
            zks.processTxn(logEntry.getHeader(), logEntry.getTxn());
        }
    }

}
