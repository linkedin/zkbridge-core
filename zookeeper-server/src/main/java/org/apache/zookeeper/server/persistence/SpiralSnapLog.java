package org.apache.zookeeper.server.persistence;

import static org.apache.zookeeper.spiral.SpiralBucket.LAST_PROCESSED_OFFSET;
import static org.apache.zookeeper.spiral.SpiralBucket.SNAPSHOT_STATUS;

import java.io.IOException;

import java.util.List;
import org.apache.jute.Record;
import org.apache.zookeeper.server.DataTree;
import org.apache.zookeeper.server.persistence.SpiralTxnLog.SpiralTxnIterator;
import org.apache.zookeeper.spiral.SpiralClient;
import org.apache.zookeeper.txn.ServerAwareTxnHeader;
import org.apache.zookeeper.util.MappingUtils;
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
     * serialized onto disk
     * @return the snapshot file
     * @throws IOException
     */
    public boolean save(
        DataTree dataTree, long serverId) throws IOException {
            // Creating a new spiral bucket by name "snapshot_<lastZxid>" to store the snapshot.
            // The lastZxid represents datatree's zxid when snapshot was started.
            long lastZxid = dataTree.lastProcessedZxid;
            LOG.info("Snapshotting at: 0x{} on spiral", Long.toHexString(lastZxid));
            snapLog.serialize(dataTree, serverId);
            return true;
        }

    public long restore(DataTree dataTree, long serverId) throws IOException {
        try {
            // Decide from which snapshot to restore
            if (!spiralClient.containsKey(SNAPSHOT_STATUS.getBucketName(), String.valueOf(serverId))) {
                LOG.info("No snapshot to restore found for serverId: {}", serverId);
                return -1;
            }
            byte[] snapshotInfo_buff = spiralClient.get(SNAPSHOT_STATUS.getBucketName(), String.valueOf(serverId));
            SpiralSnapshotInfo snapshotInfo = SpiralSnapshotInfo.deserialize(snapshotInfo_buff);
            LOG.info("Restoring from 0x{} from spiral bucket :{}", snapshotInfo.getZxid(), snapshotInfo.getNodeDataBucketName());
            snapLog.deserialize(dataTree, snapshotInfo.getNodeDataBucketName(), snapshotInfo.getAclCacheBucketName());
            dataTree.lastProcessedZxid = snapshotInfo.getZxid();

            // Now read delta from transaction log and apply it to the datatree.
            long highestProcessedZxid = fastForwardFromEdits(dataTree, serverId);
            if (highestProcessedZxid == -1) {
                return snapshotInfo.getZxid();
            } else {
                return highestProcessedZxid;
            }
        } catch(Exception e) {
            LOG.error("Error while restoring snapshot", e);
        }
        return -1;
    }

    /**
     * This function will fast forward the server database to have the latest
     * transactions in it.  This is the same as restore, but only reads from
     * the transaction logs and not restores from a snapshot. This will restore
     * from shared transaction log until it reaches the last processed txid. Rest
     * of the rehydration of datatree from shared transaction log will be done
     * using SpiralSyncProcessor.
     * @param dt the datatree to write transactions to.
     * @return the highest zxid restored.
     * @throws IOException
     */
    public long fastForwardFromEdits(DataTree dt, long serverId) {
        // case: if the server is getting started for the very first time and there is no recorded offset.
        if (!spiralClient.containsKey(LAST_PROCESSED_OFFSET.getBucketName(), String.valueOf(serverId))) {
            return -1;
        }

        byte[] bytes = spiralClient.get(LAST_PROCESSED_OFFSET.getBucketName(), String.valueOf(serverId));
        Long endLastProcessedZxid = Long.valueOf(new String(bytes));
        SpiralTxnIterator txnIterator = null;
        try {
            LOG.info("Starting to fast forward using Spiral from zxid: {} to zxid :{}", dt.lastProcessedZxid + 1, endLastProcessedZxid);
            txnIterator = new SpiralTxnIterator(spiralClient, dt.lastProcessedZxid + 1, endLastProcessedZxid);
            while (txnIterator.next()) {
                ServerAwareTxnHeader hdr = txnIterator.getHeader();
                Record txn = txnIterator.getTxn();

                dt.processTxn(MappingUtils.toTxnHeader(hdr), txn, null);
            }
            LOG.info("Fast forwarded datatree from Spiral. Last processed txn Id: {}", dt.lastProcessedZxid);
            return dt.lastProcessedZxid;
        } catch (Exception e) {
            throw new RuntimeException(
                String.format("error while hydrating zkbridge server: %s while reading zxid: %s", serverId, txnIterator.getCurrZxid()), e);
        }
    }

    public int snapshotCount() {
        int count = 0;
        List<String> buckets = spiralClient.listBuckets();
        for (String bucketName: buckets) {
            if (bucketName.startsWith(SNAPSHOT_BUCKET_PREFIX)) {
                count ++;
            }
        }
        return count;
    }
}
