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

package org.apache.zookeeper.server.persistence;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.zip.Adler32;
import java.util.zip.Checksum;
import org.apache.jute.BinaryInputArchive;
import org.apache.jute.BinaryOutputArchive;
import org.apache.jute.InputArchive;
import org.apache.jute.Record;
import org.apache.zookeeper.server.Request;
import org.apache.zookeeper.server.ServerMetrics;
import org.apache.zookeeper.server.SpiralTxnLogEntry;
import org.apache.zookeeper.server.TxnLogEntry;
import org.apache.zookeeper.server.util.SerializeUtils;
import org.apache.zookeeper.spiral.SpiralBucket;
import org.apache.zookeeper.spiral.SpiralClient;
import org.apache.zookeeper.txn.ServerAwareTxnHeader;
import org.apache.zookeeper.txn.TxnDigest;
import org.apache.zookeeper.txn.TxnHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.zookeeper.spiral.InternalStateKey.*;
import static org.apache.zookeeper.spiral.SpiralBucket.*;


/**
 * This class implements the TxnLog interface. It provides api's
 * to access the txnlogs and add entries to it.
 * <p>
 * The format of a Transactional log is as follows:
 * <blockquote><pre>
 * LogFile:
 *     FileHeader TxnList ZeroPad
 *
 * FileHeader: {
 *     magic 4bytes (ZKLG)
 *     version 4bytes
 *     dbid 8bytes
 *   }
 *
 * TxnList:
 *     Txn || Txn TxnList
 *
 * Txn:
 *     checksum Txnlen TxnHeader Record 0x42
 *
 * checksum: 8bytes Adler32 is currently used
 *   calculated across payload -- Txnlen, TxnHeader, Record and 0x42
 *
 * Txnlen:
 *     len 4bytes
 *
 * TxnHeader: {
 *     sessionid 8bytes
 *     cxid 4bytes
 *     zxid 8bytes
 *     time 8bytes
 *     type 4bytes
 *   }
 *
 * Record:
 *     See Jute definition file for details on the various record types
 *
 * ZeroPad:
 *     0 padded to EOF (filled during preallocation stage)
 * </pre></blockquote>
 */
public class SpiralTxnLog {

    private static final Logger LOG = LoggerFactory.getLogger(SpiralTxnLog.class);
    private final SpiralClient spiralClient;

    /**
     * constructor for SpiralTxnLog.
     */
    public SpiralTxnLog(SpiralClient spiralClient) {
        this.spiralClient = spiralClient;
    }

    public synchronized boolean append(Long serverId, Request request) throws IOException {
        TxnHeader hdr = request.getHdr();
        if (hdr == null) {
            return false;
        }

        ServerAwareTxnHeader spiralHdr = new ServerAwareTxnHeader(hdr.getClientId(), hdr.getCxid(), hdr.getZxid(),
            hdr.getTime(), hdr.getType(), String.valueOf(serverId));

        long zxid = hdr.getZxid();
        byte[] buf = Util.marshallTxnEntry(spiralHdr, request.getTxn(), request.getTxnDigest());
        spiralClient.put(SHARED_TRANSACTION_LOG.getBucketName(), String.valueOf(zxid), buf);
        LOG.info("Appended zxid {} to global changelog", zxid);
                return true;
    }

    public static class SpiralTxnIterator {
        long startZxid;
        long currZxid;
        long endZxid;
        ServerAwareTxnHeader hdr;
        Record record;
        TxnDigest digest;
        SpiralClient spiralClient;

        /**
         * create an iterator over a gloabl transaction log store
         * @param startZxid the zxid to start reading from
         * @param endZxid the zxid to end reading until
         */
        public SpiralTxnIterator(SpiralClient spiralClient, long startZxid, long endZxid) {
            this.startZxid = startZxid;
            this.endZxid = endZxid;
            this.currZxid = startZxid;
            this.spiralClient = spiralClient;
        }

        /**
         * the iterator that moves to the next transaction
         * @return true if there is more transactions to be read
         * false if not.
         */
        public boolean next() throws IOException {
            if (currZxid > endZxid) {
                return false;
            }

            while (!spiralClient.containsKey(SHARED_TRANSACTION_LOG.getBucketName(), String.valueOf(this.currZxid))) {
                this.currZxid++;
                if (currZxid > endZxid) {
                    return false;
                }
            }

            LOG.info("[SpiralTxnIterator] -- applying entry {}", currZxid);
            byte[] txnBuf = spiralClient.get(SpiralBucket.SHARED_TRANSACTION_LOG.getBucketName(), String.valueOf(this.currZxid));
            SpiralTxnLogEntry logEntry = SerializeUtils.deserializeSpiralTxn(txnBuf);
            hdr = logEntry.getHeader();
            record = logEntry.getTxn();
            digest = logEntry.getDigest();

            this.currZxid++;
            return true;
        }

        /**
         * return the current header
         * @return the current header that
         * is read
         */
        public ServerAwareTxnHeader getHeader() {
            return hdr;
        }

        /**
         * return the current transaction
         * @return the current transaction
         * that is read
         */
        public Record getTxn() {
            return record;
        }

        public TxnDigest getDigest() {
            return digest;
        }

        /**
         * close the iterator
         * and release the resources.
         */
        public void close() throws IOException {
        }

        public long getCurrZxid() {
            return currZxid;
        }
    }

}
