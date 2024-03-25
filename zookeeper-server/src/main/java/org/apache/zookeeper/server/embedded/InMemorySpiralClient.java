package org.apache.zookeeper.server.embedded;

import com.google.protobuf.ByteString;
import java.util.List;
import java.util.Map;
import org.apache.zookeeper.spiral.SpiralBucket;
import org.apache.zookeeper.spiral.SpiralClient;
import proto.com.linkedin.spiral.Key;
import proto.com.linkedin.spiral.KeyValue;
import proto.com.linkedin.spiral.PaginationContext;
import proto.com.linkedin.spiral.ScanResponse;
import proto.com.linkedin.spiral.Value;


public class InMemorySpiralClient implements SpiralClient {

  private static final String DEFAULT_NAMESPACE = "zookeeper";
  private final InMemoryFS fs;

  public InMemorySpiralClient(InMemoryFS fs) {
    this.fs = fs;
  }

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
    fs.createBucket(bucketName);
  }

  @Override
  public boolean containsKey(String bucketName, String key) {
    return fs.containsKey(bucketName, key);
  }

  @Override
  public byte[] get(String bucketName, String key) {
    return fs.get(bucketName, key);
  }

  @Override
  public byte[] asyncGet(String bucketName, String key) {
    return fs.get(bucketName, key);
  }

  @Override
  public void updateLastProcessedTxn(long serverId, long zxid) {
    put(SpiralBucket.LAST_PROCESSED_OFFSET.getBucketName(), String.valueOf(serverId), String.valueOf(zxid).getBytes());
  }

  @Override
  public Long generateTransactionId() {
    return fs.generateTransactionId();
  }

  @Override
  public void put(String bucketName, String key, byte[] value) {
    fs.put(bucketName, key, value);
  }

  @Override
  public void delete(String bucketName, String key) {
    fs.delete(bucketName, key);
  }

  @Override
  public ScanResponse scanBucket(String bucketName, PaginationContext paginationContext) {
    Map<String, byte[]> listContent = fs.list(bucketName);
    ScanResponse.Builder responseBuilder = ScanResponse.newBuilder();

    for (String keyStr: listContent.keySet()) {
      responseBuilder
          .addKeyValues(KeyValue.newBuilder()
          .setKey(Key.newBuilder().setMessage(ByteString.copyFromUtf8(keyStr)).build())
          .setValue(Value.newBuilder().setMessage(ByteString.copyFrom(listContent.get(keyStr))).build())
          .build());
    }
    return responseBuilder.build();
  }

  @Override
  public List<String> listBuckets() {
    return fs.list();
  }
}
