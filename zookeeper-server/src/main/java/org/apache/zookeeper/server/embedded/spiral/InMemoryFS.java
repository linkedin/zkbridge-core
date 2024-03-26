package org.apache.zookeeper.server.embedded.spiral;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;


public class InMemoryFS {

  private final AtomicLong TXN_ID = new AtomicLong(0);
  private final Map<String, HashMap<String, byte[]>> spiralContent = new HashMap<>();

  public InMemoryFS() {
  }

  public Long generateTransactionId() {
    return TXN_ID.incrementAndGet();
  }

  public void createBucket(String bucket) {
    spiralContent.putIfAbsent(bucket, new HashMap<>());
  }

  public boolean containsKey(String bucket, String key) {
    return spiralContent.containsKey(bucket) && spiralContent.get(bucket).containsKey(key);
  }

  public byte[] get(String bucket, String key) {
    return spiralContent.get(bucket).get(key);
  }

  public void put(String bucket, String key, byte[] value) {
    spiralContent.computeIfAbsent(bucket, k-> new HashMap<>()).put(key, value);
  }

  public void delete(String bucket, String key) {
    spiralContent.get(bucket).remove(key);
  }

  public List<String> list() {
    return spiralContent.keySet().stream().collect(Collectors.toList());
  }

  public Map<String, byte[]> list(String bucketName) {
    return spiralContent.get(bucketName);
  }

  @Override
  public String toString() {
    return String.format("Transaction Id: %s, spiral content: %s", TXN_ID, spiralContent.keySet());
  }

}