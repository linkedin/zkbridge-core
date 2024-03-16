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

import static org.apache.zookeeper.spiral.SpiralBucket.SNAPSHOT_STATUS;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import javax.annotation.Nonnull;

import org.apache.zookeeper.common.Time;
import org.apache.zookeeper.server.DataTree;
import org.apache.zookeeper.spiral.SpiralClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * It is responsible for storing, serializing
 * and deserializing the right snapshot into Spiral
 * and provides access to the snapshots storeed inside Spiral.
 */
public class SpiralSnap {

    SnapshotInfo lastSnapshotInfo = null;
    private volatile boolean close = false;
    private static final Logger LOG = LoggerFactory.getLogger(FileSnap.class);
    private final SpiralClient spiralClient;

    public SpiralSnap(@Nonnull SpiralClient spiralClient) {
        this.spiralClient = spiralClient;
    }

    /**
     * get information of the last saved/restored snapshot
     * @return info of last snapshot
     */
    public SnapshotInfo getLastSnapshotInfo() {
        return this.lastSnapshotInfo;
    }

    /**
     * serialize the datatree and session into the file snapshot
     * @param dt the datatree to be serialized
     * @param sessions the sessions to be serialized
     * @param fsync sync the file immediately after write
     */
    public synchronized void serialize(
        DataTree dt, Map<Long, Integer> sessions, String bucketName, long serverId) throws IOException {
        if (!close) {
            // TODO: Taking a snapshot could take some time, so we want to make sure that it's either taken fully or none, hence
            // will maintain another state in bucket "SNAPSHOT_STATUS" to indicate if the snapshot is in progress or not.
            spiralClient.createBucket(bucketName);
            
            // TODO: have not added serialization of sessions yet. Add it later.
            dt.serializeOnSpiral(spiralClient, bucketName);

            // Once snapshot bucket is created, seal/commit the process of snapshotting by creating a new entry in SNAPSHOT_STATUS bucket under given serverId.
            lastSnapshotInfo = new SnapshotInfo(dt.lastProcessedZxid, Time.currentElapsedTime());
            spiralClient.put(SNAPSHOT_STATUS.getBucketName(), String.valueOf(serverId), String.valueOf(dt.lastProcessedZxid).getBytes());
        } else {
            throw new IOException("FileSnap has already been closed");
        }
    }

    /**
     * synchronized close just so that if serialize is in place
     * the close operation will block and will wait till serialize
     * is done and will set the close flag
     */
    public synchronized void close() throws IOException {
        close = true;
    }

    public long deserialize(DataTree dt, Map<Long, Integer> sessions, String bucketName) throws IOException {
        dt.deserializeFromSpiral(spiralClient, bucketName);
        // TODO: find heighest zxid from the datatree and return it
        return 0;
    }

    public File findMostRecentSnapshot() throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findMostRecentSnapshot'");
    }
}
