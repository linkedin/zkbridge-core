package org.apache.zookeeper.spiral;

import java.nio.charset.StandardCharsets;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proto.com.linkedin.spiral.BatchDeleteRequest;
import proto.com.linkedin.spiral.BatchDeleteResponse;
import proto.com.linkedin.spiral.BatchGetRequest;
import proto.com.linkedin.spiral.BatchGetResponse;
import proto.com.linkedin.spiral.BatchPutRequest;
import proto.com.linkedin.spiral.BatchPutResponse;
import proto.com.linkedin.spiral.DeleteRequest;
import proto.com.linkedin.spiral.DeleteResponse;
import proto.com.linkedin.spiral.GetRequest;
import proto.com.linkedin.spiral.GetResponse;
import proto.com.linkedin.spiral.Key;
import proto.com.linkedin.spiral.PaginationContext;
import proto.com.linkedin.spiral.Put;
import proto.com.linkedin.spiral.PutRequest;
import proto.com.linkedin.spiral.PutResponse;
import proto.com.linkedin.spiral.ScanRequest;
import proto.com.linkedin.spiral.ScanResponse;
import proto.com.linkedin.spiral.SpiralApiGrpc;
import proto.com.linkedin.spiral.SpiralContext;
import proto.com.linkedin.spiral.Value;
import proto.com.linkedin.spiral.GetRequestOrBuilder;
import proto.com.linkedin.spiral.Value;

public class SpiralClient {
  private static final Logger logger = LoggerFactory.getLogger(SpiralClient.class);

  private final SpiralApiGrpc.SpiralApiBlockingStub _blockingStub;
  private final SpiralApiGrpc.SpiralApiStub _asyncStub;
  private final SpiralContext _spiralContext;


  public SpiralClient(String spiralEndpoint) {
    try {
      ManagedChannel channel = ManagedChannelBuilder.forTarget(spiralEndpoint).usePlaintext().build();
      _blockingStub = SpiralApiGrpc.newBlockingStub(channel);
      _asyncStub = SpiralApiGrpc.newStub(channel);

      _spiralContext = SpiralContext.newBuilder()
                         .setNamespace("test")
                         .setBucket("zk")
                         .build();
      logger.info("Connected to spiral-service : {}", spiralEndpoint);
    } catch (Exception e) {
      logger.error("Failed to connect to spiral service at endpoint : {} {}", spiralEndpoint, e.getMessage());
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
