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

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.jute.Record;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.ZooDefs.OpCode;
import org.apache.zookeeper.common.Time;
import org.apache.zookeeper.server.DataTree;
import org.apache.zookeeper.server.DataTree.ProcessTxnResult;
import org.apache.zookeeper.server.Request;
import org.apache.zookeeper.server.ServerMetrics;
import org.apache.zookeeper.server.ServerStats;
import org.apache.zookeeper.server.ZooTrace;
import org.apache.zookeeper.server.persistence.TxnLog.TxnIterator;
import org.apache.zookeeper.spiral.SpiralClient;
import org.apache.zookeeper.txn.CreateSessionTxn;
import org.apache.zookeeper.txn.TxnDigest;
import org.apache.zookeeper.txn.TxnHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This is a helper class
 * above the implementations
 * of txnlog and snapshot
 * classes for Spiral
 */
public class SpiralTxnSnapLog extends FileTxnSnapLog {

    private static final Logger LOG = LoggerFactory.getLogger(SpiralTxnSnapLog.class);
    private final SpiralClient spiralClient;

    /**
     * the constructor which takes the datadir and
     * snapdir.
     * @param dataDir the transaction directory
     * @param snapDir the snapshot directory
     */
    public SpiralTxnSnapLog(SpiralClient spiralClient, File dataDir, File snapDir) throws IOException {
        super(dataDir, snapDir);
        this.spiralClient = spiralClient;
        this.txnLog = new SpiralTxnLog(spiralClient, dataDir);
    }

    /**
     * Get TxnIterator for iterating through txnlog starting at a given zxid
     *
     * @param zxid starting zxid
     * @return TxnIterator
     * @throws IOException
     */
    public TxnIterator readTxnLog(long zxid) throws IOException {
        return readTxnLog(zxid, true);
    }

    /**
     * Get TxnIterator for iterating through txnlog starting at a given zxid
     *
     * @param zxid starting zxid
     * @param fastForward true if the iterator should be fast forwarded to point
     *        to the txn of a given zxid, else the iterator will point to the
     *        starting txn of a txnlog that may contain txn of a given zxid
     * @return TxnIterator
     * @throws IOException
     */
    public TxnIterator readTxnLog(long zxid, boolean fastForward) throws IOException {
        SpiralTxnLog txnLog = new SpiralTxnLog(spiralClient, dataDir);
        return txnLog.read(zxid, fastForward);
    }


    /**
     * the last logged zxid on the transaction logs
     * @return the last logged zxid
     */
    public long getLastLoggedZxid() {
        SpiralTxnLog txnLog = new SpiralTxnLog(spiralClient, dataDir);
        return txnLog.getLastLoggedZxid();
    }

    /**
     *
     * @return elapsed sync time of transaction log commit in milliseconds
     */
    public long getTxnLogElapsedSyncTime() {
        return txnLog.getTxnLogSyncElapsedTime();
    }

}
