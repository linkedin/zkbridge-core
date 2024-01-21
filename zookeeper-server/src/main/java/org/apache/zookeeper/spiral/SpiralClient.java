package org.apache.zookeeper.spiral;

import java.io.File;
import javax.net.ssl.SSLException;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;

import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NegotiationType;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proto.com.linkedin.spiral.CreateBucketRequest;
import proto.com.linkedin.spiral.CreateNamespaceRequest;
import proto.com.linkedin.spiral.GetBucketRequest;
import proto.com.linkedin.spiral.GetNamespaceRequest;

import proto.com.linkedin.spiral.GetNamespaceResponse;
import proto.com.linkedin.spiral.GetRequest;
import proto.com.linkedin.spiral.GetResponse;
import proto.com.linkedin.spiral.Key;
import proto.com.linkedin.spiral.Put;
import proto.com.linkedin.spiral.PutRequest;
import proto.com.linkedin.spiral.PutResponse;
import proto.com.linkedin.spiral.SpiralApiGrpc;
import proto.com.linkedin.spiral.SpiralContext;
import proto.com.linkedin.spiral.Value;

public class SpiralClient {
  private static final Logger logger = LoggerFactory.getLogger(SpiralClient.class);

  private final SpiralApiGrpc.SpiralApiBlockingStub _blockingStub;
  private final SpiralApiGrpc.SpiralApiStub _asyncStub;
  private final SpiralContext _spiralContext;

  private final static String _namespace = "test";
  private final static String _bucket = "zk";

  public SpiralClient(String spiralEndpoint, String identityCert, String identityKey,
      String caBundle, String overrideAuthority) throws SSLException {
    try {

      SslContext sslContext = GrpcSslContexts.forClient()
          .trustManager(new File(caBundle))
          .keyManager(new File(identityCert),
              new File(identityKey))
          .build();

      // Create a channel using the SSL context.
      NettyChannelBuilder channelBuilder = NettyChannelBuilder.forTarget(spiralEndpoint)
          .overrideAuthority(overrideAuthority).sslContext(sslContext);
      channelBuilder.negotiationType(NegotiationType.TLS);
      ManagedChannel channel = channelBuilder.build();

      //ManagedChannel channel = ManagedChannelBuilder.forTarget(spiralEndpoint).usePlaintext().build();
      _blockingStub = SpiralApiGrpc.newBlockingStub(channel);
      _asyncStub = SpiralApiGrpc.newStub(channel);
      // verify namespace and bucket exists
      verifySpiralContextExists();
      _spiralContext = SpiralContext.newBuilder()
                         .setNamespace(_namespace)
                         .setBucket(_bucket)
                         .build();
      logger.info("Connected to spiral-service : {}", spiralEndpoint);
    } catch (Exception e) {
      logger.error("Failed to connect to spiral service at endpoint : {} {}", spiralEndpoint, e.getMessage());
      throw e;
    }
  }

  // TODO - We are not suppose to create the zookeeper namespace in the real world.
  // But we are not there yet, so we will verify that namespace / bucket exists.
  public void verifySpiralContextExists() {
    try {
      GetNamespaceRequest request = GetNamespaceRequest.newBuilder().setName(_namespace).build();
      try {
        GetNamespaceResponse response = _blockingStub.getNamespace(request);
      } catch (Exception e) {
        logger.error("ZKBridge Namespace: test is not yet created");
        createNamespace();
      }

      GetBucketRequest bucketRequest =
          GetBucketRequest.newBuilder().setNamespace(_namespace).setName(_bucket).build();
      try {
        _blockingStub.getBucket(bucketRequest);
      } catch (Exception e) {
        logger.error("ZKBridge Bucket is not yet created");
        createBucket();
      }

    } catch (Exception e) {
      logger.error("Failed to create namespace : {} bucket : {}", _namespace, _bucket);
      throw e;
    }
  }

  // create namespace
  private void createNamespace() {
    try {
      CreateNamespaceRequest request = CreateNamespaceRequest.newBuilder().setName(_namespace).build();
      _blockingStub.createNamespace(request);
    } catch (Exception e) {
      logger.error("Failed to create namespace : {}", _namespace);
      throw e;
    }
  }

  // create bucket
  private void createBucket() {
    try {
      CreateBucketRequest
          request = CreateBucketRequest.newBuilder().setNamespace(_namespace).setName(_bucket).build();
      _blockingStub.createBucket(request);
    } catch (Exception e) {
      logger.error("Failed to create bucket : {}", _bucket);
      throw e;
    }
  }

  public byte[] get(String key) {
    try {
      ByteString keyBytes = ByteString.copyFromUtf8(key);
      Key apiKey = Key.newBuilder().setMessage(keyBytes).build();
      GetRequest request = GetRequest.newBuilder().setSpiralContext(_spiralContext).setKey(apiKey).build();
      GetResponse response = _blockingStub.get(request);
      return response.getValue().getMessage().toByteArray();
    } catch (Exception e) {
      logger.error("Get: RPC failed: {}", e.getMessage());
      throw e;
    }
  }

  public byte[] asyncGet(String key) {
    final byte[][] value = new byte[1][];
    try {
      ByteString keyBytes = ByteString.copyFromUtf8(key);
      Key apiKey = Key.newBuilder().setMessage(keyBytes).build();
      GetRequest request = GetRequest.newBuilder()
          .setSpiralContext(_spiralContext).setKey(apiKey).build();
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
      logger.error("Get: RPC failed: {}", e.getMessage());
      throw e;
    }
    return value[0];
  }

  public void put(String key, byte[] value) {
    try {
      byte[] keyBytes = key.getBytes();
      //ByteString keyBytes = ByteString.copyFromUtf8(key);
      Key apiKey = Key.newBuilder().setMessage(ByteString.copyFrom(keyBytes)).build();
      Value apiValue = Value.newBuilder().setMessage(ByteString.copyFrom(value)).build();
      Put putValue = Put.newBuilder().setKey(apiKey).setValue(apiValue).build();
      PutRequest request = PutRequest.newBuilder()
          .setSpiralContext(_spiralContext)
          .setPut(putValue)
          .build();

      // TODO - convert response to ZK response.
      PutResponse response = _blockingStub.put(request);
    } catch (Exception e) {
      logger.error("put: RPC failed: {}, {}", e.getMessage(), e);
      throw e;
    }
  }
}
