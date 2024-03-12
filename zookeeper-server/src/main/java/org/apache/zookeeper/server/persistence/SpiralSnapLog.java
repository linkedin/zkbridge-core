package org.apache.zookeeper.server.persistence;

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

    public SpiralSnapLog(SpiralClient spiralClient) throws IOException {
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
        ConcurrentHashMap<Long, Integer> sessionsWithTimeouts) throws IOException {
            long lastZxid = dataTree.lastProcessedZxid;
            String snapBucket = Util.makeSnapshotName(lastZxid);
            LOG.info("Snapshotting: 0x{} to {}", Long.toHexString(lastZxid), snapBucket);
            snapLog.serialize(dataTree, sessionsWithTimeouts);
            return true;
        }
}
