package org.apache.zookeeper.server.embedded;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.zookeeper.spiral.SpiralBucket;
import org.apache.zookeeper.spiral.SpiralClient;
import proto.com.linkedin.spiral.PaginationContext;
import proto.com.linkedin.spiral.ScanResponse;


public class SpiralEmbeddedClient implements SpiralClient {

  private static final String DEFAULT_NAMESPACE = "zookeeper";
  private static final AtomicLong TXN_ID = new AtomicLong(0);
  private static Map<String, HashMap<String, byte[]>> spiralContent = new HashMap<>();
  @Override
  public void initialize() {
    createNamespace(DEFAULT_NAMESPACE);
    for (SpiralBucket bucket : SpiralBucket.values()) {
      createBucket(bucket.getBucketName());
    }
  }

  @Override
  public void createNamespace(String namespace) {
    // no-op
    return;
  }

  @Override
  public void createBucket(String bucketName) {
    spiralContent.putIfAbsent(bucketName, new HashMap<String, byte[]>());
  }

  @Override
  public boolean containsKey(String bucketName, String key) {
    return spiralContent.containsKey(bucketName);
  }

  @Override
  public byte[] get(String bucketName, String key) {
    return spiralContent.get(bucketName).get(key);
  }

  @Override
  public byte[] asyncGet(String bucketName, String key) {
    return spiralContent.get(bucketName).get(key);
  }

  @Override
  public void updateLastProcessedTxn(long serverId, long zxid) {
    put(SpiralBucket.LAST_PROCESSED_OFFSET.getBucketName(), String.valueOf(serverId), String.valueOf(zxid).getBytes());
  }

  @Override
  public Long generateTransactionId() {
    return TXN_ID.incrementAndGet();
  }

  @Override
  public void put(String bucketName, String key, byte[] value) {
    spiralContent.get(bucketName).put(key, value);
  }

  @Override
  public void delete(String bucketName, String key) {
    spiralContent.get(bucketName).remove(key);
  }

  @Override
  public ScanResponse scanBucket(String bucketName, PaginationContext paginationContext) {
    return null;
  }
}
