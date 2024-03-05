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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.SessionExpiredException;
import org.apache.zookeeper.common.Time;
import org.apache.zookeeper.spiral.SpiralBucket;
import org.apache.zookeeper.spiral.SpiralClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a full featured SessionTracker. It tracks session in grouped by tick
 * interval. It always rounds up the tick interval to provide a sort of grace
 * period. Sessions are thus expired in batches made up of sessions that expire
 * in a given interval.
 */
public class SpiralSessionTrackerImpl extends SessionTrackerImpl {

    private static final Logger LOG = LoggerFactory.getLogger(SessionTrackerImpl.class);
    private final SpiralClient spiralClient;

    public SpiralSessionTrackerImpl(SessionExpirer expirer, ConcurrentMap<Long, Integer> sessionsWithTimeout, int tickTime, long serverId, ZooKeeperServerListener listener, SpiralClient spiralClient) {
        super(expirer, sessionsWithTimeout, tickTime, serverId, listener);
        this.spiralClient = spiralClient;
    }
    
    public long createSession(long sessionId, ConnectRequest sessionInfo) {
        spiralClient.put(SpiralBucket.SESSIONS, sessionId, sessionInfo);
        return 0;
    }

    public void closeSession(long sessionId) {
        spiralClient.delete(SpiralBucket.SESSIONS, sessionId);
    }
}
