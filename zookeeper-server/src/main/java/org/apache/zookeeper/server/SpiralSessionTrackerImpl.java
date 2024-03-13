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

import java.util.concurrent.ConcurrentMap;
import org.apache.zookeeper.spiral.SpiralBucket;
import org.apache.zookeeper.spiral.SpiralClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Spiral Session tracker which maintains the session information in spiral.
 */
public class SpiralSessionTrackerImpl extends SessionTrackerImpl {

    private static final Logger LOG = LoggerFactory.getLogger(SpiralSessionTrackerImpl.class);

    private final SpiralClient spiralClient;

    public SpiralSessionTrackerImpl(SessionExpirer expirer, ConcurrentMap<Long, Integer> sessionsWithTimeout, int tickTime, long serverId, ZooKeeperServerListener listener, SpiralClient spiralClient) {
        super(expirer, sessionsWithTimeout, tickTime, serverId, listener);
        this.spiralClient = spiralClient;
    }

    public long createSession(long sessionId, int timeout) {
        LOG.info("Creating spiral entry for session 0x{}", Long.toHexString(sessionId));
        spiralClient.put(SpiralBucket.SESSIONS.getBucketName(), String.valueOf(sessionId), String.valueOf(timeout).getBytes());
        return sessionId;
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
        if (sessionsById.containsKey(sessionId)) {
            super.updateSessionExpiry(sessionsById.get(sessionId), timeout);
            return true;
        }

        LOG.debug("Checking session 0x{} from spiral", Long.toHexString(sessionId));
        byte[] txnBytes = spiralClient.get(SpiralBucket.SESSIONS.getBucketName(), String.valueOf(sessionId));
        Integer oldTimeout = Integer.valueOf(new String(txnBytes));

        if (oldTimeout < timeout) {
            spiralClient.put(SpiralBucket.SESSIONS.getBucketName(), String.valueOf(sessionId), String.valueOf(timeout).getBytes());
        }

        return super.trackSession(sessionId, timeout);
    }
}
