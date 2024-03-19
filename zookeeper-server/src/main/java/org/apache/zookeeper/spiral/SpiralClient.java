package org.apache.zookeeper.spiral;

public interface SpiralClient {

  void initialize();

  void createNamespace(String namespace);

  void createBucket(String bucketName);

  boolean containsKey(String bucketName, String key);

  byte[] get(String bucketName, String key);

  byte[] asyncGet(String bucketName, String key);

  void updateLastProcessedTxn(long serverId, long zxid);

  Long generateTransactionId();

  void put(String bucketName, String key, byte[] value);

  void delete(String bucketName, String key);
}
