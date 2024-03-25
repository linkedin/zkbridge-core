package org.apache.zookeeper.server.persistence;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/*
 * Stores all information related to snapshot stored in Spiral.
 * This includes the bucket names for node data and acl cache and the timestamp of the snapshot.
 */
public class SpiralSnapshotInfo implements Serializable {
    public String nodeDataBucketName;
    public String aclCacheBucketName;
    public long timestamp;
    public long zxid;

    SpiralSnapshotInfo(long zxid, long timestamp) {
        this.zxid = zxid;
        this.nodeDataBucketName = "node_data_" + String.valueOf(zxid);
        this.aclCacheBucketName = "acl_cache_" + String.valueOf(zxid);
        this.timestamp = timestamp;
    }

    public byte[] serialize() {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
            ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(this);
            return bos.toByteArray();
        } catch(Exception e) {
            throw new RuntimeException("Error serializing SpiralSnapshotInfo", e);
        }
    }

    public static SpiralSnapshotInfo deserialize(byte[] data) {
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return (SpiralSnapshotInfo) ois.readObject();
        } catch(Exception e) {
            throw new RuntimeException("Error deserializing SpiralSnapshotInfo", e);
        }
    }

    public String getNodeDataBucketName() {
        return nodeDataBucketName;
    }

    public String getAclCacheBucketName() {
        return aclCacheBucketName;
    }

    public long getZxid() {
        return zxid;
    }
}
