// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: SpiralApi.proto

package proto.com.linkedin.spiral;

/**
 * Protobuf type {@code proto.com.linkedin.spiral.GetBucketResponse}
 */
public  final class GetBucketResponse extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:proto.com.linkedin.spiral.GetBucketResponse)
    GetBucketResponseOrBuilder {
  // Use GetBucketResponse.newBuilder() to construct.
  private GetBucketResponse(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private GetBucketResponse() {
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
  }
  private GetBucketResponse(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    int mutable_bitField0_ = 0;
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          default: {
            if (!input.skipField(tag)) {
              done = true;
            }
            break;
          }
          case 10: {
            proto.com.linkedin.spiral.Bucket.Builder subBuilder = null;
            if (bucket_ != null) {
              subBuilder = bucket_.toBuilder();
            }
            bucket_ = input.readMessage(proto.com.linkedin.spiral.Bucket.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(bucket_);
              bucket_ = subBuilder.buildPartial();
            }

            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return proto.com.linkedin.spiral.SpiralApiOuterClass.internal_static_proto_com_linkedin_spiral_GetBucketResponse_descriptor;
  }

  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return proto.com.linkedin.spiral.SpiralApiOuterClass.internal_static_proto_com_linkedin_spiral_GetBucketResponse_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            proto.com.linkedin.spiral.GetBucketResponse.class, proto.com.linkedin.spiral.GetBucketResponse.Builder.class);
  }

  public static final int BUCKET_FIELD_NUMBER = 1;
  private proto.com.linkedin.spiral.Bucket bucket_;
  /**
   * <code>.proto.com.linkedin.spiral.Bucket bucket = 1;</code>
   */
  public boolean hasBucket() {
    return bucket_ != null;
  }
  /**
   * <code>.proto.com.linkedin.spiral.Bucket bucket = 1;</code>
   */
  public proto.com.linkedin.spiral.Bucket getBucket() {
    return bucket_ == null ? proto.com.linkedin.spiral.Bucket.getDefaultInstance() : bucket_;
  }
  /**
   * <code>.proto.com.linkedin.spiral.Bucket bucket = 1;</code>
   */
  public proto.com.linkedin.spiral.BucketOrBuilder getBucketOrBuilder() {
    return getBucket();
  }

  private byte memoizedIsInitialized = -1;
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (bucket_ != null) {
      output.writeMessage(1, getBucket());
    }
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (bucket_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, getBucket());
    }
    memoizedSize = size;
    return size;
  }

  private static final long serialVersionUID = 0L;
  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof proto.com.linkedin.spiral.GetBucketResponse)) {
      return super.equals(obj);
    }
    proto.com.linkedin.spiral.GetBucketResponse other = (proto.com.linkedin.spiral.GetBucketResponse) obj;

    boolean result = true;
    result = result && (hasBucket() == other.hasBucket());
    if (hasBucket()) {
      result = result && getBucket()
          .equals(other.getBucket());
    }
    return result;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    if (hasBucket()) {
      hash = (37 * hash) + BUCKET_FIELD_NUMBER;
      hash = (53 * hash) + getBucket().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static proto.com.linkedin.spiral.GetBucketResponse parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static proto.com.linkedin.spiral.GetBucketResponse parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static proto.com.linkedin.spiral.GetBucketResponse parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static proto.com.linkedin.spiral.GetBucketResponse parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static proto.com.linkedin.spiral.GetBucketResponse parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static proto.com.linkedin.spiral.GetBucketResponse parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static proto.com.linkedin.spiral.GetBucketResponse parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static proto.com.linkedin.spiral.GetBucketResponse parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static proto.com.linkedin.spiral.GetBucketResponse parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static proto.com.linkedin.spiral.GetBucketResponse parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(proto.com.linkedin.spiral.GetBucketResponse prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code proto.com.linkedin.spiral.GetBucketResponse}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:proto.com.linkedin.spiral.GetBucketResponse)
      proto.com.linkedin.spiral.GetBucketResponseOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return proto.com.linkedin.spiral.SpiralApiOuterClass.internal_static_proto_com_linkedin_spiral_GetBucketResponse_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return proto.com.linkedin.spiral.SpiralApiOuterClass.internal_static_proto_com_linkedin_spiral_GetBucketResponse_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              proto.com.linkedin.spiral.GetBucketResponse.class, proto.com.linkedin.spiral.GetBucketResponse.Builder.class);
    }

    // Construct using proto.com.linkedin.spiral.GetBucketResponse.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    public Builder clear() {
      super.clear();
      if (bucketBuilder_ == null) {
        bucket_ = null;
      } else {
        bucket_ = null;
        bucketBuilder_ = null;
      }
      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return proto.com.linkedin.spiral.SpiralApiOuterClass.internal_static_proto_com_linkedin_spiral_GetBucketResponse_descriptor;
    }

    public proto.com.linkedin.spiral.GetBucketResponse getDefaultInstanceForType() {
      return proto.com.linkedin.spiral.GetBucketResponse.getDefaultInstance();
    }

    public proto.com.linkedin.spiral.GetBucketResponse build() {
      proto.com.linkedin.spiral.GetBucketResponse result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public proto.com.linkedin.spiral.GetBucketResponse buildPartial() {
      proto.com.linkedin.spiral.GetBucketResponse result = new proto.com.linkedin.spiral.GetBucketResponse(this);
      if (bucketBuilder_ == null) {
        result.bucket_ = bucket_;
      } else {
        result.bucket_ = bucketBuilder_.build();
      }
      onBuilt();
      return result;
    }

    public Builder clone() {
      return (Builder) super.clone();
    }
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return (Builder) super.setField(field, value);
    }
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return (Builder) super.clearField(field);
    }
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return (Builder) super.clearOneof(oneof);
    }
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, Object value) {
      return (Builder) super.setRepeatedField(field, index, value);
    }
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return (Builder) super.addRepeatedField(field, value);
    }
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof proto.com.linkedin.spiral.GetBucketResponse) {
        return mergeFrom((proto.com.linkedin.spiral.GetBucketResponse)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(proto.com.linkedin.spiral.GetBucketResponse other) {
      if (other == proto.com.linkedin.spiral.GetBucketResponse.getDefaultInstance()) return this;
      if (other.hasBucket()) {
        mergeBucket(other.getBucket());
      }
      onChanged();
      return this;
    }

    public final boolean isInitialized() {
      return true;
    }

    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      proto.com.linkedin.spiral.GetBucketResponse parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (proto.com.linkedin.spiral.GetBucketResponse) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private proto.com.linkedin.spiral.Bucket bucket_ = null;
    private com.google.protobuf.SingleFieldBuilderV3<
        proto.com.linkedin.spiral.Bucket, proto.com.linkedin.spiral.Bucket.Builder, proto.com.linkedin.spiral.BucketOrBuilder> bucketBuilder_;
    /**
     * <code>.proto.com.linkedin.spiral.Bucket bucket = 1;</code>
     */
    public boolean hasBucket() {
      return bucketBuilder_ != null || bucket_ != null;
    }
    /**
     * <code>.proto.com.linkedin.spiral.Bucket bucket = 1;</code>
     */
    public proto.com.linkedin.spiral.Bucket getBucket() {
      if (bucketBuilder_ == null) {
        return bucket_ == null ? proto.com.linkedin.spiral.Bucket.getDefaultInstance() : bucket_;
      } else {
        return bucketBuilder_.getMessage();
      }
    }
    /**
     * <code>.proto.com.linkedin.spiral.Bucket bucket = 1;</code>
     */
    public Builder setBucket(proto.com.linkedin.spiral.Bucket value) {
      if (bucketBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        bucket_ = value;
        onChanged();
      } else {
        bucketBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <code>.proto.com.linkedin.spiral.Bucket bucket = 1;</code>
     */
    public Builder setBucket(
        proto.com.linkedin.spiral.Bucket.Builder builderForValue) {
      if (bucketBuilder_ == null) {
        bucket_ = builderForValue.build();
        onChanged();
      } else {
        bucketBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <code>.proto.com.linkedin.spiral.Bucket bucket = 1;</code>
     */
    public Builder mergeBucket(proto.com.linkedin.spiral.Bucket value) {
      if (bucketBuilder_ == null) {
        if (bucket_ != null) {
          bucket_ =
            proto.com.linkedin.spiral.Bucket.newBuilder(bucket_).mergeFrom(value).buildPartial();
        } else {
          bucket_ = value;
        }
        onChanged();
      } else {
        bucketBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <code>.proto.com.linkedin.spiral.Bucket bucket = 1;</code>
     */
    public Builder clearBucket() {
      if (bucketBuilder_ == null) {
        bucket_ = null;
        onChanged();
      } else {
        bucket_ = null;
        bucketBuilder_ = null;
      }

      return this;
    }
    /**
     * <code>.proto.com.linkedin.spiral.Bucket bucket = 1;</code>
     */
    public proto.com.linkedin.spiral.Bucket.Builder getBucketBuilder() {
      
      onChanged();
      return getBucketFieldBuilder().getBuilder();
    }
    /**
     * <code>.proto.com.linkedin.spiral.Bucket bucket = 1;</code>
     */
    public proto.com.linkedin.spiral.BucketOrBuilder getBucketOrBuilder() {
      if (bucketBuilder_ != null) {
        return bucketBuilder_.getMessageOrBuilder();
      } else {
        return bucket_ == null ?
            proto.com.linkedin.spiral.Bucket.getDefaultInstance() : bucket_;
      }
    }
    /**
     * <code>.proto.com.linkedin.spiral.Bucket bucket = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        proto.com.linkedin.spiral.Bucket, proto.com.linkedin.spiral.Bucket.Builder, proto.com.linkedin.spiral.BucketOrBuilder> 
        getBucketFieldBuilder() {
      if (bucketBuilder_ == null) {
        bucketBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            proto.com.linkedin.spiral.Bucket, proto.com.linkedin.spiral.Bucket.Builder, proto.com.linkedin.spiral.BucketOrBuilder>(
                getBucket(),
                getParentForChildren(),
                isClean());
        bucket_ = null;
      }
      return bucketBuilder_;
    }
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return this;
    }

    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return this;
    }


    // @@protoc_insertion_point(builder_scope:proto.com.linkedin.spiral.GetBucketResponse)
  }

  // @@protoc_insertion_point(class_scope:proto.com.linkedin.spiral.GetBucketResponse)
  private static final proto.com.linkedin.spiral.GetBucketResponse DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new proto.com.linkedin.spiral.GetBucketResponse();
  }

  public static proto.com.linkedin.spiral.GetBucketResponse getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<GetBucketResponse>
      PARSER = new com.google.protobuf.AbstractParser<GetBucketResponse>() {
    public GetBucketResponse parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return new GetBucketResponse(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<GetBucketResponse> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<GetBucketResponse> getParserForType() {
    return PARSER;
  }

  public proto.com.linkedin.spiral.GetBucketResponse getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

