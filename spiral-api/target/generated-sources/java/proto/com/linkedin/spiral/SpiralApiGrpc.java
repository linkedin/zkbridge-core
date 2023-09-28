package proto.com.linkedin.spiral;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.0.1)",
    comments = "Source: SpiralApi.proto")
public class SpiralApiGrpc {

  private SpiralApiGrpc() {}

  public static final String SERVICE_NAME = "proto.com.linkedin.spiral.SpiralApi";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<proto.com.linkedin.spiral.CreateNamespaceRequest,
      proto.com.linkedin.spiral.CreateNamespaceResponse> METHOD_CREATE_NAMESPACE =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "proto.com.linkedin.spiral.SpiralApi", "createNamespace"),
          io.grpc.protobuf.ProtoUtils.marshaller(proto.com.linkedin.spiral.CreateNamespaceRequest.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(proto.com.linkedin.spiral.CreateNamespaceResponse.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<proto.com.linkedin.spiral.GetNamespaceRequest,
      proto.com.linkedin.spiral.GetNamespaceResponse> METHOD_GET_NAMESPACE =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "proto.com.linkedin.spiral.SpiralApi", "getNamespace"),
          io.grpc.protobuf.ProtoUtils.marshaller(proto.com.linkedin.spiral.GetNamespaceRequest.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(proto.com.linkedin.spiral.GetNamespaceResponse.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<proto.com.linkedin.spiral.ListNamespaceRequest,
      proto.com.linkedin.spiral.ListNamespaceResponse> METHOD_LIST_NAMESPACES =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "proto.com.linkedin.spiral.SpiralApi", "listNamespaces"),
          io.grpc.protobuf.ProtoUtils.marshaller(proto.com.linkedin.spiral.ListNamespaceRequest.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(proto.com.linkedin.spiral.ListNamespaceResponse.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<proto.com.linkedin.spiral.DeleteNamespaceRequest,
      proto.com.linkedin.spiral.DeleteNamespaceResponse> METHOD_DELETE_NAMESPACE =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "proto.com.linkedin.spiral.SpiralApi", "deleteNamespace"),
          io.grpc.protobuf.ProtoUtils.marshaller(proto.com.linkedin.spiral.DeleteNamespaceRequest.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(proto.com.linkedin.spiral.DeleteNamespaceResponse.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<proto.com.linkedin.spiral.CreateBucketRequest,
      proto.com.linkedin.spiral.CreateBucketResponse> METHOD_CREATE_BUCKET =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "proto.com.linkedin.spiral.SpiralApi", "createBucket"),
          io.grpc.protobuf.ProtoUtils.marshaller(proto.com.linkedin.spiral.CreateBucketRequest.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(proto.com.linkedin.spiral.CreateBucketResponse.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<proto.com.linkedin.spiral.GetBucketRequest,
      proto.com.linkedin.spiral.GetBucketResponse> METHOD_GET_BUCKET =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "proto.com.linkedin.spiral.SpiralApi", "getBucket"),
          io.grpc.protobuf.ProtoUtils.marshaller(proto.com.linkedin.spiral.GetBucketRequest.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(proto.com.linkedin.spiral.GetBucketResponse.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<proto.com.linkedin.spiral.DeleteBucketRequest,
      proto.com.linkedin.spiral.DeleteBucketResponse> METHOD_DELETE_BUCKET =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "proto.com.linkedin.spiral.SpiralApi", "deleteBucket"),
          io.grpc.protobuf.ProtoUtils.marshaller(proto.com.linkedin.spiral.DeleteBucketRequest.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(proto.com.linkedin.spiral.DeleteBucketResponse.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<proto.com.linkedin.spiral.ListBucketsRequest,
      proto.com.linkedin.spiral.ListBucketsResponse> METHOD_LIST_BUCKETS =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "proto.com.linkedin.spiral.SpiralApi", "listBuckets"),
          io.grpc.protobuf.ProtoUtils.marshaller(proto.com.linkedin.spiral.ListBucketsRequest.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(proto.com.linkedin.spiral.ListBucketsResponse.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<proto.com.linkedin.spiral.GetRequest,
      proto.com.linkedin.spiral.GetResponse> METHOD_GET =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "proto.com.linkedin.spiral.SpiralApi", "get"),
          io.grpc.protobuf.ProtoUtils.marshaller(proto.com.linkedin.spiral.GetRequest.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(proto.com.linkedin.spiral.GetResponse.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<proto.com.linkedin.spiral.PutRequest,
      proto.com.linkedin.spiral.PutResponse> METHOD_PUT =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "proto.com.linkedin.spiral.SpiralApi", "put"),
          io.grpc.protobuf.ProtoUtils.marshaller(proto.com.linkedin.spiral.PutRequest.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(proto.com.linkedin.spiral.PutResponse.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<proto.com.linkedin.spiral.ScanRequest,
      proto.com.linkedin.spiral.ScanResponse> METHOD_SCAN =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "proto.com.linkedin.spiral.SpiralApi", "scan"),
          io.grpc.protobuf.ProtoUtils.marshaller(proto.com.linkedin.spiral.ScanRequest.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(proto.com.linkedin.spiral.ScanResponse.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<proto.com.linkedin.spiral.DeleteRequest,
      proto.com.linkedin.spiral.DeleteResponse> METHOD_DELETE =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "proto.com.linkedin.spiral.SpiralApi", "delete"),
          io.grpc.protobuf.ProtoUtils.marshaller(proto.com.linkedin.spiral.DeleteRequest.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(proto.com.linkedin.spiral.DeleteResponse.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<proto.com.linkedin.spiral.BatchGetRequest,
      proto.com.linkedin.spiral.BatchGetResponse> METHOD_BATCH_GET =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "proto.com.linkedin.spiral.SpiralApi", "batchGet"),
          io.grpc.protobuf.ProtoUtils.marshaller(proto.com.linkedin.spiral.BatchGetRequest.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(proto.com.linkedin.spiral.BatchGetResponse.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<proto.com.linkedin.spiral.BatchPutRequest,
      proto.com.linkedin.spiral.BatchPutResponse> METHOD_BATCH_PUT =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "proto.com.linkedin.spiral.SpiralApi", "batchPut"),
          io.grpc.protobuf.ProtoUtils.marshaller(proto.com.linkedin.spiral.BatchPutRequest.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(proto.com.linkedin.spiral.BatchPutResponse.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<proto.com.linkedin.spiral.BatchDeleteRequest,
      proto.com.linkedin.spiral.BatchDeleteResponse> METHOD_BATCH_DELETE =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "proto.com.linkedin.spiral.SpiralApi", "batchDelete"),
          io.grpc.protobuf.ProtoUtils.marshaller(proto.com.linkedin.spiral.BatchDeleteRequest.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(proto.com.linkedin.spiral.BatchDeleteResponse.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<proto.com.linkedin.spiral.RangeDeleteRequest,
      proto.com.linkedin.spiral.RangeDeleteResponse> METHOD_RANGE_DELETE =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "proto.com.linkedin.spiral.SpiralApi", "rangeDelete"),
          io.grpc.protobuf.ProtoUtils.marshaller(proto.com.linkedin.spiral.RangeDeleteRequest.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(proto.com.linkedin.spiral.RangeDeleteResponse.getDefaultInstance()));

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static SpiralApiStub newStub(io.grpc.Channel channel) {
    return new SpiralApiStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static SpiralApiBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new SpiralApiBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary and streaming output calls on the service
   */
  public static SpiralApiFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new SpiralApiFutureStub(channel);
  }

  /**
   */
  public static abstract class SpiralApiImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Namespace management APIs
     * </pre>
     */
    public void createNamespace(proto.com.linkedin.spiral.CreateNamespaceRequest request,
        io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.CreateNamespaceResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_CREATE_NAMESPACE, responseObserver);
    }

    /**
     */
    public void getNamespace(proto.com.linkedin.spiral.GetNamespaceRequest request,
        io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.GetNamespaceResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_GET_NAMESPACE, responseObserver);
    }

    /**
     */
    public void listNamespaces(proto.com.linkedin.spiral.ListNamespaceRequest request,
        io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.ListNamespaceResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_LIST_NAMESPACES, responseObserver);
    }

    /**
     */
    public void deleteNamespace(proto.com.linkedin.spiral.DeleteNamespaceRequest request,
        io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.DeleteNamespaceResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_DELETE_NAMESPACE, responseObserver);
    }

    /**
     * <pre>
     * Bucket management APIs
     * </pre>
     */
    public void createBucket(proto.com.linkedin.spiral.CreateBucketRequest request,
        io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.CreateBucketResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_CREATE_BUCKET, responseObserver);
    }

    /**
     */
    public void getBucket(proto.com.linkedin.spiral.GetBucketRequest request,
        io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.GetBucketResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_GET_BUCKET, responseObserver);
    }

    /**
     */
    public void deleteBucket(proto.com.linkedin.spiral.DeleteBucketRequest request,
        io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.DeleteBucketResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_DELETE_BUCKET, responseObserver);
    }

    /**
     */
    public void listBuckets(proto.com.linkedin.spiral.ListBucketsRequest request,
        io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.ListBucketsResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_LIST_BUCKETS, responseObserver);
    }

    /**
     * <pre>
     * Core APIs
     * </pre>
     */
    public void get(proto.com.linkedin.spiral.GetRequest request,
        io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.GetResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_GET, responseObserver);
    }

    /**
     */
    public void put(proto.com.linkedin.spiral.PutRequest request,
        io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.PutResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_PUT, responseObserver);
    }

    /**
     */
    public void scan(proto.com.linkedin.spiral.ScanRequest request,
        io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.ScanResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_SCAN, responseObserver);
    }

    /**
     */
    public void delete(proto.com.linkedin.spiral.DeleteRequest request,
        io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.DeleteResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_DELETE, responseObserver);
    }

    /**
     */
    public void batchGet(proto.com.linkedin.spiral.BatchGetRequest request,
        io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.BatchGetResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_BATCH_GET, responseObserver);
    }

    /**
     */
    public void batchPut(proto.com.linkedin.spiral.BatchPutRequest request,
        io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.BatchPutResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_BATCH_PUT, responseObserver);
    }

    /**
     */
    public void batchDelete(proto.com.linkedin.spiral.BatchDeleteRequest request,
        io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.BatchDeleteResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_BATCH_DELETE, responseObserver);
    }

    /**
     */
    public void rangeDelete(proto.com.linkedin.spiral.RangeDeleteRequest request,
        io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.RangeDeleteResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_RANGE_DELETE, responseObserver);
    }

    @java.lang.Override public io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_CREATE_NAMESPACE,
            asyncUnaryCall(
              new MethodHandlers<
                proto.com.linkedin.spiral.CreateNamespaceRequest,
                proto.com.linkedin.spiral.CreateNamespaceResponse>(
                  this, METHODID_CREATE_NAMESPACE)))
          .addMethod(
            METHOD_GET_NAMESPACE,
            asyncUnaryCall(
              new MethodHandlers<
                proto.com.linkedin.spiral.GetNamespaceRequest,
                proto.com.linkedin.spiral.GetNamespaceResponse>(
                  this, METHODID_GET_NAMESPACE)))
          .addMethod(
            METHOD_LIST_NAMESPACES,
            asyncUnaryCall(
              new MethodHandlers<
                proto.com.linkedin.spiral.ListNamespaceRequest,
                proto.com.linkedin.spiral.ListNamespaceResponse>(
                  this, METHODID_LIST_NAMESPACES)))
          .addMethod(
            METHOD_DELETE_NAMESPACE,
            asyncUnaryCall(
              new MethodHandlers<
                proto.com.linkedin.spiral.DeleteNamespaceRequest,
                proto.com.linkedin.spiral.DeleteNamespaceResponse>(
                  this, METHODID_DELETE_NAMESPACE)))
          .addMethod(
            METHOD_CREATE_BUCKET,
            asyncUnaryCall(
              new MethodHandlers<
                proto.com.linkedin.spiral.CreateBucketRequest,
                proto.com.linkedin.spiral.CreateBucketResponse>(
                  this, METHODID_CREATE_BUCKET)))
          .addMethod(
            METHOD_GET_BUCKET,
            asyncUnaryCall(
              new MethodHandlers<
                proto.com.linkedin.spiral.GetBucketRequest,
                proto.com.linkedin.spiral.GetBucketResponse>(
                  this, METHODID_GET_BUCKET)))
          .addMethod(
            METHOD_DELETE_BUCKET,
            asyncUnaryCall(
              new MethodHandlers<
                proto.com.linkedin.spiral.DeleteBucketRequest,
                proto.com.linkedin.spiral.DeleteBucketResponse>(
                  this, METHODID_DELETE_BUCKET)))
          .addMethod(
            METHOD_LIST_BUCKETS,
            asyncUnaryCall(
              new MethodHandlers<
                proto.com.linkedin.spiral.ListBucketsRequest,
                proto.com.linkedin.spiral.ListBucketsResponse>(
                  this, METHODID_LIST_BUCKETS)))
          .addMethod(
            METHOD_GET,
            asyncUnaryCall(
              new MethodHandlers<
                proto.com.linkedin.spiral.GetRequest,
                proto.com.linkedin.spiral.GetResponse>(
                  this, METHODID_GET)))
          .addMethod(
            METHOD_PUT,
            asyncUnaryCall(
              new MethodHandlers<
                proto.com.linkedin.spiral.PutRequest,
                proto.com.linkedin.spiral.PutResponse>(
                  this, METHODID_PUT)))
          .addMethod(
            METHOD_SCAN,
            asyncUnaryCall(
              new MethodHandlers<
                proto.com.linkedin.spiral.ScanRequest,
                proto.com.linkedin.spiral.ScanResponse>(
                  this, METHODID_SCAN)))
          .addMethod(
            METHOD_DELETE,
            asyncUnaryCall(
              new MethodHandlers<
                proto.com.linkedin.spiral.DeleteRequest,
                proto.com.linkedin.spiral.DeleteResponse>(
                  this, METHODID_DELETE)))
          .addMethod(
            METHOD_BATCH_GET,
            asyncUnaryCall(
              new MethodHandlers<
                proto.com.linkedin.spiral.BatchGetRequest,
                proto.com.linkedin.spiral.BatchGetResponse>(
                  this, METHODID_BATCH_GET)))
          .addMethod(
            METHOD_BATCH_PUT,
            asyncUnaryCall(
              new MethodHandlers<
                proto.com.linkedin.spiral.BatchPutRequest,
                proto.com.linkedin.spiral.BatchPutResponse>(
                  this, METHODID_BATCH_PUT)))
          .addMethod(
            METHOD_BATCH_DELETE,
            asyncUnaryCall(
              new MethodHandlers<
                proto.com.linkedin.spiral.BatchDeleteRequest,
                proto.com.linkedin.spiral.BatchDeleteResponse>(
                  this, METHODID_BATCH_DELETE)))
          .addMethod(
            METHOD_RANGE_DELETE,
            asyncUnaryCall(
              new MethodHandlers<
                proto.com.linkedin.spiral.RangeDeleteRequest,
                proto.com.linkedin.spiral.RangeDeleteResponse>(
                  this, METHODID_RANGE_DELETE)))
          .build();
    }
  }

  /**
   */
  public static final class SpiralApiStub extends io.grpc.stub.AbstractStub<SpiralApiStub> {
    private SpiralApiStub(io.grpc.Channel channel) {
      super(channel);
    }

    private SpiralApiStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SpiralApiStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new SpiralApiStub(channel, callOptions);
    }

    /**
     * <pre>
     * Namespace management APIs
     * </pre>
     */
    public void createNamespace(proto.com.linkedin.spiral.CreateNamespaceRequest request,
        io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.CreateNamespaceResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_CREATE_NAMESPACE, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getNamespace(proto.com.linkedin.spiral.GetNamespaceRequest request,
        io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.GetNamespaceResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_GET_NAMESPACE, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listNamespaces(proto.com.linkedin.spiral.ListNamespaceRequest request,
        io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.ListNamespaceResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_LIST_NAMESPACES, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deleteNamespace(proto.com.linkedin.spiral.DeleteNamespaceRequest request,
        io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.DeleteNamespaceResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_DELETE_NAMESPACE, getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Bucket management APIs
     * </pre>
     */
    public void createBucket(proto.com.linkedin.spiral.CreateBucketRequest request,
        io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.CreateBucketResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_CREATE_BUCKET, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getBucket(proto.com.linkedin.spiral.GetBucketRequest request,
        io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.GetBucketResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_GET_BUCKET, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deleteBucket(proto.com.linkedin.spiral.DeleteBucketRequest request,
        io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.DeleteBucketResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_DELETE_BUCKET, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listBuckets(proto.com.linkedin.spiral.ListBucketsRequest request,
        io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.ListBucketsResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_LIST_BUCKETS, getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Core APIs
     * </pre>
     */
    public void get(proto.com.linkedin.spiral.GetRequest request,
        io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.GetResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_GET, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void put(proto.com.linkedin.spiral.PutRequest request,
        io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.PutResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_PUT, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void scan(proto.com.linkedin.spiral.ScanRequest request,
        io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.ScanResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_SCAN, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void delete(proto.com.linkedin.spiral.DeleteRequest request,
        io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.DeleteResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_DELETE, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void batchGet(proto.com.linkedin.spiral.BatchGetRequest request,
        io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.BatchGetResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_BATCH_GET, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void batchPut(proto.com.linkedin.spiral.BatchPutRequest request,
        io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.BatchPutResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_BATCH_PUT, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void batchDelete(proto.com.linkedin.spiral.BatchDeleteRequest request,
        io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.BatchDeleteResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_BATCH_DELETE, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void rangeDelete(proto.com.linkedin.spiral.RangeDeleteRequest request,
        io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.RangeDeleteResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_RANGE_DELETE, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class SpiralApiBlockingStub extends io.grpc.stub.AbstractStub<SpiralApiBlockingStub> {
    private SpiralApiBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private SpiralApiBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SpiralApiBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new SpiralApiBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Namespace management APIs
     * </pre>
     */
    public proto.com.linkedin.spiral.CreateNamespaceResponse createNamespace(proto.com.linkedin.spiral.CreateNamespaceRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_CREATE_NAMESPACE, getCallOptions(), request);
    }

    /**
     */
    public proto.com.linkedin.spiral.GetNamespaceResponse getNamespace(proto.com.linkedin.spiral.GetNamespaceRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_GET_NAMESPACE, getCallOptions(), request);
    }

    /**
     */
    public proto.com.linkedin.spiral.ListNamespaceResponse listNamespaces(proto.com.linkedin.spiral.ListNamespaceRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_LIST_NAMESPACES, getCallOptions(), request);
    }

    /**
     */
    public proto.com.linkedin.spiral.DeleteNamespaceResponse deleteNamespace(proto.com.linkedin.spiral.DeleteNamespaceRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_DELETE_NAMESPACE, getCallOptions(), request);
    }

    /**
     * <pre>
     * Bucket management APIs
     * </pre>
     */
    public proto.com.linkedin.spiral.CreateBucketResponse createBucket(proto.com.linkedin.spiral.CreateBucketRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_CREATE_BUCKET, getCallOptions(), request);
    }

    /**
     */
    public proto.com.linkedin.spiral.GetBucketResponse getBucket(proto.com.linkedin.spiral.GetBucketRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_GET_BUCKET, getCallOptions(), request);
    }

    /**
     */
    public proto.com.linkedin.spiral.DeleteBucketResponse deleteBucket(proto.com.linkedin.spiral.DeleteBucketRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_DELETE_BUCKET, getCallOptions(), request);
    }

    /**
     */
    public proto.com.linkedin.spiral.ListBucketsResponse listBuckets(proto.com.linkedin.spiral.ListBucketsRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_LIST_BUCKETS, getCallOptions(), request);
    }

    /**
     * <pre>
     * Core APIs
     * </pre>
     */
    public proto.com.linkedin.spiral.GetResponse get(proto.com.linkedin.spiral.GetRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_GET, getCallOptions(), request);
    }

    /**
     */
    public proto.com.linkedin.spiral.PutResponse put(proto.com.linkedin.spiral.PutRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_PUT, getCallOptions(), request);
    }

    /**
     */
    public proto.com.linkedin.spiral.ScanResponse scan(proto.com.linkedin.spiral.ScanRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_SCAN, getCallOptions(), request);
    }

    /**
     */
    public proto.com.linkedin.spiral.DeleteResponse delete(proto.com.linkedin.spiral.DeleteRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_DELETE, getCallOptions(), request);
    }

    /**
     */
    public proto.com.linkedin.spiral.BatchGetResponse batchGet(proto.com.linkedin.spiral.BatchGetRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_BATCH_GET, getCallOptions(), request);
    }

    /**
     */
    public proto.com.linkedin.spiral.BatchPutResponse batchPut(proto.com.linkedin.spiral.BatchPutRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_BATCH_PUT, getCallOptions(), request);
    }

    /**
     */
    public proto.com.linkedin.spiral.BatchDeleteResponse batchDelete(proto.com.linkedin.spiral.BatchDeleteRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_BATCH_DELETE, getCallOptions(), request);
    }

    /**
     */
    public proto.com.linkedin.spiral.RangeDeleteResponse rangeDelete(proto.com.linkedin.spiral.RangeDeleteRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_RANGE_DELETE, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class SpiralApiFutureStub extends io.grpc.stub.AbstractStub<SpiralApiFutureStub> {
    private SpiralApiFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private SpiralApiFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SpiralApiFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new SpiralApiFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Namespace management APIs
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.com.linkedin.spiral.CreateNamespaceResponse> createNamespace(
        proto.com.linkedin.spiral.CreateNamespaceRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_CREATE_NAMESPACE, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.com.linkedin.spiral.GetNamespaceResponse> getNamespace(
        proto.com.linkedin.spiral.GetNamespaceRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_GET_NAMESPACE, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.com.linkedin.spiral.ListNamespaceResponse> listNamespaces(
        proto.com.linkedin.spiral.ListNamespaceRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_LIST_NAMESPACES, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.com.linkedin.spiral.DeleteNamespaceResponse> deleteNamespace(
        proto.com.linkedin.spiral.DeleteNamespaceRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_DELETE_NAMESPACE, getCallOptions()), request);
    }

    /**
     * <pre>
     * Bucket management APIs
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.com.linkedin.spiral.CreateBucketResponse> createBucket(
        proto.com.linkedin.spiral.CreateBucketRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_CREATE_BUCKET, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.com.linkedin.spiral.GetBucketResponse> getBucket(
        proto.com.linkedin.spiral.GetBucketRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_GET_BUCKET, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.com.linkedin.spiral.DeleteBucketResponse> deleteBucket(
        proto.com.linkedin.spiral.DeleteBucketRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_DELETE_BUCKET, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.com.linkedin.spiral.ListBucketsResponse> listBuckets(
        proto.com.linkedin.spiral.ListBucketsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_LIST_BUCKETS, getCallOptions()), request);
    }

    /**
     * <pre>
     * Core APIs
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.com.linkedin.spiral.GetResponse> get(
        proto.com.linkedin.spiral.GetRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_GET, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.com.linkedin.spiral.PutResponse> put(
        proto.com.linkedin.spiral.PutRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_PUT, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.com.linkedin.spiral.ScanResponse> scan(
        proto.com.linkedin.spiral.ScanRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_SCAN, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.com.linkedin.spiral.DeleteResponse> delete(
        proto.com.linkedin.spiral.DeleteRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_DELETE, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.com.linkedin.spiral.BatchGetResponse> batchGet(
        proto.com.linkedin.spiral.BatchGetRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_BATCH_GET, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.com.linkedin.spiral.BatchPutResponse> batchPut(
        proto.com.linkedin.spiral.BatchPutRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_BATCH_PUT, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.com.linkedin.spiral.BatchDeleteResponse> batchDelete(
        proto.com.linkedin.spiral.BatchDeleteRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_BATCH_DELETE, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.com.linkedin.spiral.RangeDeleteResponse> rangeDelete(
        proto.com.linkedin.spiral.RangeDeleteRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_RANGE_DELETE, getCallOptions()), request);
    }
  }

  private static final int METHODID_CREATE_NAMESPACE = 0;
  private static final int METHODID_GET_NAMESPACE = 1;
  private static final int METHODID_LIST_NAMESPACES = 2;
  private static final int METHODID_DELETE_NAMESPACE = 3;
  private static final int METHODID_CREATE_BUCKET = 4;
  private static final int METHODID_GET_BUCKET = 5;
  private static final int METHODID_DELETE_BUCKET = 6;
  private static final int METHODID_LIST_BUCKETS = 7;
  private static final int METHODID_GET = 8;
  private static final int METHODID_PUT = 9;
  private static final int METHODID_SCAN = 10;
  private static final int METHODID_DELETE = 11;
  private static final int METHODID_BATCH_GET = 12;
  private static final int METHODID_BATCH_PUT = 13;
  private static final int METHODID_BATCH_DELETE = 14;
  private static final int METHODID_RANGE_DELETE = 15;

  private static class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final SpiralApiImplBase serviceImpl;
    private final int methodId;

    public MethodHandlers(SpiralApiImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CREATE_NAMESPACE:
          serviceImpl.createNamespace((proto.com.linkedin.spiral.CreateNamespaceRequest) request,
              (io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.CreateNamespaceResponse>) responseObserver);
          break;
        case METHODID_GET_NAMESPACE:
          serviceImpl.getNamespace((proto.com.linkedin.spiral.GetNamespaceRequest) request,
              (io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.GetNamespaceResponse>) responseObserver);
          break;
        case METHODID_LIST_NAMESPACES:
          serviceImpl.listNamespaces((proto.com.linkedin.spiral.ListNamespaceRequest) request,
              (io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.ListNamespaceResponse>) responseObserver);
          break;
        case METHODID_DELETE_NAMESPACE:
          serviceImpl.deleteNamespace((proto.com.linkedin.spiral.DeleteNamespaceRequest) request,
              (io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.DeleteNamespaceResponse>) responseObserver);
          break;
        case METHODID_CREATE_BUCKET:
          serviceImpl.createBucket((proto.com.linkedin.spiral.CreateBucketRequest) request,
              (io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.CreateBucketResponse>) responseObserver);
          break;
        case METHODID_GET_BUCKET:
          serviceImpl.getBucket((proto.com.linkedin.spiral.GetBucketRequest) request,
              (io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.GetBucketResponse>) responseObserver);
          break;
        case METHODID_DELETE_BUCKET:
          serviceImpl.deleteBucket((proto.com.linkedin.spiral.DeleteBucketRequest) request,
              (io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.DeleteBucketResponse>) responseObserver);
          break;
        case METHODID_LIST_BUCKETS:
          serviceImpl.listBuckets((proto.com.linkedin.spiral.ListBucketsRequest) request,
              (io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.ListBucketsResponse>) responseObserver);
          break;
        case METHODID_GET:
          serviceImpl.get((proto.com.linkedin.spiral.GetRequest) request,
              (io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.GetResponse>) responseObserver);
          break;
        case METHODID_PUT:
          serviceImpl.put((proto.com.linkedin.spiral.PutRequest) request,
              (io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.PutResponse>) responseObserver);
          break;
        case METHODID_SCAN:
          serviceImpl.scan((proto.com.linkedin.spiral.ScanRequest) request,
              (io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.ScanResponse>) responseObserver);
          break;
        case METHODID_DELETE:
          serviceImpl.delete((proto.com.linkedin.spiral.DeleteRequest) request,
              (io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.DeleteResponse>) responseObserver);
          break;
        case METHODID_BATCH_GET:
          serviceImpl.batchGet((proto.com.linkedin.spiral.BatchGetRequest) request,
              (io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.BatchGetResponse>) responseObserver);
          break;
        case METHODID_BATCH_PUT:
          serviceImpl.batchPut((proto.com.linkedin.spiral.BatchPutRequest) request,
              (io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.BatchPutResponse>) responseObserver);
          break;
        case METHODID_BATCH_DELETE:
          serviceImpl.batchDelete((proto.com.linkedin.spiral.BatchDeleteRequest) request,
              (io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.BatchDeleteResponse>) responseObserver);
          break;
        case METHODID_RANGE_DELETE:
          serviceImpl.rangeDelete((proto.com.linkedin.spiral.RangeDeleteRequest) request,
              (io.grpc.stub.StreamObserver<proto.com.linkedin.spiral.RangeDeleteResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    return new io.grpc.ServiceDescriptor(SERVICE_NAME,
        METHOD_CREATE_NAMESPACE,
        METHOD_GET_NAMESPACE,
        METHOD_LIST_NAMESPACES,
        METHOD_DELETE_NAMESPACE,
        METHOD_CREATE_BUCKET,
        METHOD_GET_BUCKET,
        METHOD_DELETE_BUCKET,
        METHOD_LIST_BUCKETS,
        METHOD_GET,
        METHOD_PUT,
        METHOD_SCAN,
        METHOD_DELETE,
        METHOD_BATCH_GET,
        METHOD_BATCH_PUT,
        METHOD_BATCH_DELETE,
        METHOD_RANGE_DELETE);
  }

}
