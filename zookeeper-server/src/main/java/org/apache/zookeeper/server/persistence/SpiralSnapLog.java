package org.apache.zookeeper.server.persistence;

import static org.apache.zookeeper.spiral.SpiralBucket.SNAPSHOT_STATUS;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.zookeeper.server.DataTree;
import org.apache.zookeeper.spiral.SpiralClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Exposes API to manage snapshots on Spiral.
public class SpiralSnapLog {
    private static final Logger LOG = LoggerFactory.getLogger(SpiralSnapLog.class);
    private SpiralSnap snapLog = null;
    private SpiralClient spiralClient = null;
    private static final String SNAPSHOT_BUCKET_PREFIX = "snapshot_";

    public SpiralSnapLog(SpiralClient spiralClient) throws IOException {
        this.spiralClient = spiralClient;
        this.snapLog = new SpiralSnap(spiralClient);
    }

    /**
     * save the datatree and the sessions into a snapshot
     * @param dataTree the datatree to be serialized onto disk
     * @param sessionsWithTimeouts the session timeouts to be
     * serialized onto disk
     * @param syncSnap sync the snapshot immediately after write
     * @return the snapshot file
     * @throws IOException
     */
    public boolean save(
        DataTree dataTree,
        ConcurrentHashMap<Long, Integer> sessionsWithTimeouts, long serverId) throws IOException {
            // Creating a new spiral bucket by name "snapshot_<lastZxid>" to store the snapshot.
            long lastZxid = dataTree.lastProcessedZxid;
            String snapBucket = SNAPSHOT_BUCKET_PREFIX + Long.toHexString(lastZxid);
            LOG.info("Snapshotting: 0x{} to {}", Long.toHexString(lastZxid), snapBucket);
            snapLog.serialize(dataTree, sessionsWithTimeouts, snapBucket, serverId);
            return true;
        }

    public long restore(DataTree dataTree, ConcurrentHashMap<Long, Integer> sessionsWithTimeouts, long serverId) throws IOException {
        try {
        // Decide from which snapshot to restore
        if (!spiralClient.containsKey(SNAPSHOT_STATUS.getBucketName(), String.valueOf(serverId))) {
            LOG.info("No snapshot to restore found for serverId: {}", serverId);
            return -1;
        }
        byte[] lastZxid_buff = spiralClient.get(SNAPSHOT_STATUS.getBucketName(), String.valueOf(serverId));
        long snapshot_start_zxid = Long.valueOf(new String(lastZxid_buff));
        String snapBucket = SNAPSHOT_BUCKET_PREFIX + Long.toHexString(snapshot_start_zxid);
        LOG.info("Restoring: 0x{} from spira bucket :{}", Long.toHexString(snapshot_start_zxid), snapBucket);
        return snapLog.deserialize(dataTree, sessionsWithTimeouts, snapBucket);
        } catch(Exception e) {
            LOG.error("Error while restoring snapshot", e);
        }
        return -1;
    }
}
