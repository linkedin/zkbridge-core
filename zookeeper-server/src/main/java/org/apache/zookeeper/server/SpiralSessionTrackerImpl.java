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

import java.io.ByteArrayInputStream;
import java.util.concurrent.ConcurrentMap;

import org.apache.jute.BinaryInputArchive;
import org.apache.jute.InputArchive;
import org.apache.jute.Record;
import org.apache.zookeeper.ZooDefs.OpCode;
import org.apache.zookeeper.spiral.SpiralBucket;
import org.apache.zookeeper.spiral.SpiralClient;
import org.apache.zookeeper.txn.CreateSessionTxn;
import org.apache.zookeeper.txn.TxnHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Spiral Session tracker which maintains the session information in spiral.
 */
public class SpiralSessionTrackerImpl extends SessionTrackerImpl {

    private static final Logger LOG = LoggerFactory.getLogger(SpiralSessionTrackerImpl.class);
    private static final String CreateSessionTxn = null;
    private final SpiralClient spiralClient;

    public SpiralSessionTrackerImpl(SessionExpirer expirer, ConcurrentMap<Long, Integer> sessionsWithTimeout, int tickTime, long serverId, ZooKeeperServerListener listener, SpiralClient spiralClient) {
        super(expirer, sessionsWithTimeout, tickTime, serverId, listener);
        this.spiralClient = spiralClient;
    }
    
    public long createSession(long sessionId, Request sessionReq) {
        LOG.info("Creating spiral entry for session 0x{}", Long.toHexString(sessionId));
        spiralClient.put(SpiralBucket.SESSIONS.getBucketName(), String.valueOf(sessionId), sessionReq.getSerializeData());
        return 0;
    }

    public void closeSession(long sessionId) {
        LOG.info("Removing spiral entry for session 0x{}", Long.toHexString(sessionId));
        spiralClient.delete(SpiralBucket.SESSIONS.getBucketName(), String.valueOf(sessionId));
    }

    /*
     * Fetches a session from Spiral Session bucket. Normally, during rehydration from shared transaction logs, the
     * session entry should be found in local session map but if it's lagging behind then any session handover request
     * needs to be honored. Hence this method is used to fetch the session from Spiral.
     */
    public synchronized boolean touchSession(long sessionId, int timeout) {
        SessionImpl s = sessionsById.get(sessionId);

        if (s == null) {
            try {
                // Read from Spiral
                TxnHeader hdr = new TxnHeader();
                Record txn = new CreateSessionTxn();
                long old_timeout;
                LOG.debug("Checking session 0x{} from spiral", Long.toHexString(sessionId));
                byte[] txn_bytes = spiralClient.get(SpiralBucket.SESSIONS.getBucketName(), String.valueOf(sessionId));
                final ByteArrayInputStream bais = new ByteArrayInputStream(txn_bytes);
                InputArchive ia = BinaryInputArchive.getArchive(bais);
                hdr.deserialize(ia, "hdr");
                if (hdr.getType() != OpCode.createSession) {
                    logTraceTouchInvalidSession(sessionId, timeout);
                    return false;
                }
                txn.deserialize(ia, "txn");
                LOG.debug("RR: hdr = {}, txn = {}", hdr, txn);
                
                old_timeout = ((CreateSessionTxn) txn).getTimeOut();

                // TODO: Need to add check for closing.
                 // Update session entry in Spiral if new timeout is different. 
                if (old_timeout != timeout) {
                    ((CreateSessionTxn)txn).setTimeOut(timeout);
                    // TODO: Need to check last zxid received in client request and update it here instead of reading stale zxid from Spiral.
                    Request sessionReq = new Request(sessionId, 0, OpCode.createSession, hdr, txn, hdr.getZxid());
                    spiralClient.put(SpiralBucket.SESSIONS.getBucketName(), String.valueOf(sessionId), sessionReq.getSerializeData());
                }
                return super.trackSession(sessionId, timeout);
                
            } catch (Exception e) {
                LOG.error("Error while touchSession: {}", e);
                logTraceTouchInvalidSession(sessionId, timeout);
                return false;
            }
        }
        else {
            super.updateSessionExpiry(s, timeout);
            return true;
        }   
    }
}
