package org.apache.zookeeper.spiral;

/**
 * Represents the buckets present within the ZKBridge.
 */
public enum SpiralBucket {

  SHARED_TRANSACTION_LOG ("shared_transaction_log"),
  SESSIONS("sessions");

  private final String name;

  private SpiralBucket(String name) {
    this.name = name;
  }

  public String getBucketName() {
    return name;
  }
}
