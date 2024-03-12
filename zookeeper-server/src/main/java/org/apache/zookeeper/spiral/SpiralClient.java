package org.apache.zookeeper.spiral;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NegotiationType;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;
import io.grpc.stub.StreamObserver;
import java.io.File;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proto.com.linkedin.spiral.CreateBucketRequest;
import proto.com.linkedin.spiral.CreateNamespaceRequest;
import proto.com.linkedin.spiral.GetBucketRequest;
import proto.com.linkedin.spiral.GetNamespaceRequest;
import proto.com.linkedin.spiral.GetRequest;
import proto.com.linkedin.spiral.GetResponse;
import proto.com.linkedin.spiral.Key;
import proto.com.linkedin.spiral.Put;
import proto.com.linkedin.spiral.PutRequest;
import proto.com.linkedin.spiral.PutResponse;
import proto.com.linkedin.spiral.DeleteRequest;
import proto.com.linkedin.spiral.DeleteResponse;
import proto.com.linkedin.spiral.SpiralApiGrpc;
import proto.com.linkedin.spiral.SpiralContext;
import proto.com.linkedin.spiral.Value;

import static org.apache.zookeeper.spiral.InternalStateKey.*;
import static org.apache.zookeeper.spiral.SpiralBucket.*;


public class SpiralClient {
  private static final Logger LOGGER = LoggerFactory.getLogger(SpiralClient.class);
  private static final String DEFAULT_NAMESPACE = "zookeeper";
  private static final String EMPTY_STRING = "EMPTY_VALUE";

  private final String _namespace;
  private final SpiralApiGrpc.SpiralApiBlockingStub _blockingStub;
  private final SpiralApiGrpc.SpiralApiStub _asyncStub;

  private SpiralClient(String spiralEndpoint, String identityCert, String identityKey,
      String caBundle, String overrideAuthority, String namespace) {
    try {
      SslContext sslContext = GrpcSslContexts.forClient()
          .trustManager(new File(caBundle))
          .keyManager(new File(identityCert), new File(identityKey))
          .build();

      // Create a channel using the SSL context.
      NettyChannelBuilder channelBuilder = NettyChannelBuilder
          .forTarget(spiralEndpoint)
          .overrideAuthority(overrideAuthority)
          .sslContext(sslContext);

      channelBuilder.negotiationType(NegotiationType.TLS);
      ManagedChannel channel = channelBuilder.build();

      //ManagedChannel channel = ManagedChannelBuilder.forTarget(spiralEndpoint).usePlaintext().build();
      _blockingStub = SpiralApiGrpc.newBlockingStub(channel);
      _asyncStub = SpiralApiGrpc.newStub(channel);
      _namespace = namespace == null ? DEFAULT_NAMESPACE : namespace;

      // verify namespace and buckets exists
      verifySpiralContextExists();
      LOGGER.info("Connected to spiral-service : {}", spiralEndpoint);
    } catch (Exception e) {
      LOGGER.error("Failed to connect to spiral service at endpoint : {}", spiralEndpoint, e);
      throw new RuntimeException(String.format("Failed to connect to spiral service at endpoint : %s", spiralEndpoint), e);
    }
  }

  // TODO - We are not suppose to create the zookeeper namespace in the real world.
  // But we are not there yet, so we will verify that namespace / bucket exists.
  public void verifySpiralContextExists() {
    createNamespace(_namespace);
    for (SpiralBucket bucket : SpiralBucket.values()) {
      createBucket(bucket.getBucketName());
    }
  }

  /**
   * Creates namespace
   */
  private void createNamespace(String namespace) {
    if (validateNamespaceExists(namespace)) {
      return;
    }

    try {
      CreateNamespaceRequest request = CreateNamespaceRequest.newBuilder().setName(namespace).build();
      _blockingStub.createNamespace(request);
    } catch (Exception e) {
      LOGGER.error("Failed to create namespace : {}, {}", namespace, e.getMessage(), e);
      throw e;
    }
  }

  /**
   * Validate whether namespace exists or not.
   */
  private boolean validateNamespaceExists(String namespace) {
    GetNamespaceRequest request = GetNamespaceRequest.newBuilder().setName(namespace).build();
    try {
      return _blockingStub.getNamespace(request).hasNamespace();
    } catch (Exception e) {
      LOGGER.error("Namespace: {} is not yet created", namespace, e);
    }
    return false;
  }

  /**
   * Creates Spiral bucket
   */
  public void createBucket(String bucketName) {
    if (validateBucketExists(bucketName)) {
      return;
    }

    try {
      CreateBucketRequest request = CreateBucketRequest
          .newBuilder()
          .setNamespace(_namespace)
          .setName(bucketName)
          .build();
      LOGGER.error("Creating bucket : {}", bucketName);
      _blockingStub.createBucket(request);
    } catch (Exception e) {
      LOGGER.error("Failed to create bucket : {}", bucketName, e);
      throw e;
    }
  }

  public boolean validateBucketExists(String bucketName) {
    try {
      GetBucketRequest bucketRequest = GetBucketRequest.newBuilder()
          .setNamespace(_namespace)
          .setName(bucketName)
          .build();

      return _blockingStub.getBucket(bucketRequest).hasBucket();
    } catch (Exception e) {
      LOGGER.error("ZKBridge Bucket is not yet created", e);
    }
    return false;
  }

  public boolean containsKey(String bucketName, String key) {
    try {
      return getResponse(bucketName, key).hasValue();
    } catch (Exception e) {
      LOGGER.error("ContainsKey: RPC failed or bucket: {}, key: {}", bucketName, key, e);
    }
    return false;
  }

  public byte[] get(String bucketName, String key) {
    try {
      GetResponse response = getResponse(bucketName, key);
      return response.getValue().getMessage().toByteArray();
    } catch (Exception e) {
      LOGGER.error("Get: RPC failed or bucket: {}, key: {}", bucketName, key, e);
      throw e;
    }
  }

  GetResponse getResponse(String bucketName, String key) {
    try {
      SpiralContext spiralContext = SpiralContext.newBuilder()
          .setNamespace(_namespace)
          .setBucket(bucketName)
          .build();

      ByteString keyBytes = ByteString.copyFromUtf8(key);
      Key apiKey = Key.newBuilder().setMessage(keyBytes).build();
      GetRequest request = GetRequest.newBuilder().setSpiralContext(spiralContext).setKey(apiKey).build();
      return _blockingStub.get(request);
    } catch (Exception e) {
      LOGGER.error("Get: RPC failed for bucket: {}, key: {}", bucketName, key, e);
      throw e;
    }
  }

  public byte[] asyncGet(String bucketName, String key) {
    final byte[][] value = new byte[1][];
    try {
      SpiralContext spiralContext = SpiralContext.newBuilder()
          .setNamespace(_namespace)
          .setBucket(bucketName)
          .build();

      ByteString keyBytes = ByteString.copyFromUtf8(key);
      Key apiKey = Key.newBuilder().setMessage(keyBytes).build();
      GetRequest request = GetRequest.newBuilder().setSpiralContext(spiralContext).setKey(apiKey).build();

      // async call
      _asyncStub.get(request, new StreamObserver<GetResponse>() {
        @Override
        public void onNext(GetResponse response) {
          value[0] = response.getValue().getMessage().toByteArray();
        }
        @Override
        public void onError(Throwable t) {
          value[0] = null;
        }
        @Override
        public void onCompleted() {
        }
      });
    } catch (Exception e) {
      LOGGER.error("Get: RPC failed for bucket: {}, key: {}", bucketName, key, e);
      throw e;
    }
    return value[0];
  }

  public void updateLastProcessedTxn(long serverId, long zxid) {
    put(SpiralBucket.LAST_PROCESSED_OFFSET.getBucketName(), String.valueOf(serverId), String.valueOf(zxid).getBytes());
  }

  public Long generateTransactionId() {
    if (!containsKey(INTERNAL_STATE.getBucketName(), LATEST_TRANSACTION_ID.name())) {
      put(INTERNAL_STATE.getBucketName(), LATEST_TRANSACTION_ID.name(), String.valueOf(1).getBytes());
      return 1L;
    }

    byte[] lastZxidBuf = get(INTERNAL_STATE.getBucketName(), LATEST_TRANSACTION_ID.name());
    Long nextZxid = Long.valueOf(new String(lastZxidBuf)) + 1;
    put(INTERNAL_STATE.getBucketName(), LATEST_TRANSACTION_ID.name(), String.valueOf(nextZxid).getBytes());
    LOGGER.info("Generated new Transaction Id using Spiral: {}", nextZxid);
    return nextZxid;
  }

  public void put(String bucketName, String key, byte[] value) {
    try {
      SpiralContext spiralContext = SpiralContext.newBuilder()
          .setNamespace(_namespace)
          .setBucket(bucketName)
          .build();

      byte[] keyBytes = key.getBytes();
      //ByteString keyBytes = ByteString.copyFromUtf8(key);
      Key apiKey = Key.newBuilder().setMessage(ByteString.copyFrom(keyBytes)).build();
      // TODO: dserialize this back to empty string in GET method.
      if (value.length == 0) {
        value = EMPTY_STRING.getBytes();
      }
      Value apiValue = Value.newBuilder().setMessage(ByteString.copyFrom(value)).build();
      Put putValue = Put.newBuilder().setKey(apiKey).setValue(apiValue).build();
      PutRequest request = PutRequest.newBuilder()
          .setSpiralContext(spiralContext)
          .setPut(putValue)
          .build();

      // TODO - convert response to ZK response.
      PutResponse response = _blockingStub.put(request);
    } catch (Exception e) {
      LOGGER.error("Put: RPC failed for bucket: {}, key: {}", bucketName, key, e);
      throw e;
    }
  }

  public void delete(String bucketName, String key) {
    try {
      SpiralContext spiralContext = SpiralContext.newBuilder()
          .setNamespace(_namespace)
          .setBucket(bucketName)
          .build();

      ByteString keyBytes = ByteString.copyFromUtf8(key);
      Key apiKey = Key.newBuilder().setMessage(keyBytes).build();
      DeleteRequest request = DeleteRequest.newBuilder().setSpiralContext(spiralContext).setKey(apiKey).build();
      
      // TODO: check for valid response
      DeleteResponse response = _blockingStub.delete(request);
      // LOGGER.info("Delete: RPC response for bucket: {}, key: {}", bucketName, key, response);
    } catch (Exception e) {
      LOGGER.error("Delete: RPC failed for bucket: {}, key: {}", bucketName, key, e);
      throw e;
    }
  }

  // Builder Class
  public static class SpiralClientBuilder {

    private String spiralEndpoint;
    private String identityCert;
    private String identityKey;
    private String caBundle;
    private String overrideAuthority;
    private String namespace;

    public SpiralClientBuilder setSpiralEndpoint(String spiralEndpoint) {
      this.spiralEndpoint = spiralEndpoint;
      return this;
    }

    public SpiralClientBuilder setIdentityCert(String identityCert) {
      this.identityCert = identityCert;
      return this;
    }

    public SpiralClientBuilder setIdentityKey(String identityKey) {
      this.identityKey = identityKey;
      return this;
    }

    public SpiralClientBuilder setCaBundle(String caBundle) {
      this.caBundle = caBundle;
      return this;
    }

    public SpiralClientBuilder setOverrideAuthority(String overrideAuthority) {
      this.overrideAuthority = overrideAuthority;
      return this;
    }

    public SpiralClientBuilder setNamespace(String namespace) {
      this.namespace = namespace;
      return this;
    }

    public SpiralClient build() {
      Objects.requireNonNull(spiralEndpoint, "Spiral endpoint cannot be null");
      Objects.requireNonNull(identityCert, "identity cert for Spiral cannot be null");
      Objects.requireNonNull(identityKey, "identity key for Spiral cannot be null");
      Objects.requireNonNull(caBundle, "CA bundle for Spiral cannot be null");
      Objects.requireNonNull(overrideAuthority, "Override auth for spiral cannot be null");
      Objects.requireNonNull(namespace, "Spiral namespace cannot be null");

      return new SpiralClient(spiralEndpoint, identityCert, identityKey, caBundle, overrideAuthority, namespace);
    }

  }
}
