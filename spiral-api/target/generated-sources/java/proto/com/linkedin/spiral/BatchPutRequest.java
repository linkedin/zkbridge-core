// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: SpiralApi.proto

package proto.com.linkedin.spiral;

/**
 * Protobuf type {@code proto.com.linkedin.spiral.BatchPutRequest}
 */
public  final class BatchPutRequest extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:proto.com.linkedin.spiral.BatchPutRequest)
    BatchPutRequestOrBuilder {
  // Use BatchPutRequest.newBuilder() to construct.
  private BatchPutRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private BatchPutRequest() {
    put_ = java.util.Collections.emptyList();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
  }
  private BatchPutRequest(
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
            proto.com.linkedin.spiral.SpiralContext.Builder subBuilder = null;
            if (spiralContext_ != null) {
              subBuilder = spiralContext_.toBuilder();
            }
            spiralContext_ = input.readMessage(proto.com.linkedin.spiral.SpiralContext.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(spiralContext_);
              spiralContext_ = subBuilder.buildPartial();
            }

            break;
          }
          case 18: {
            if (!((mutable_bitField0_ & 0x00000002) == 0x00000002)) {
              put_ = new java.util.ArrayList<proto.com.linkedin.spiral.Put>();
              mutable_bitField0_ |= 0x00000002;
            }
            put_.add(
                input.readMessage(proto.com.linkedin.spiral.Put.parser(), extensionRegistry));
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
      if (((mutable_bitField0_ & 0x00000002) == 0x00000002)) {
        put_ = java.util.Collections.unmodifiableList(put_);
      }
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return proto.com.linkedin.spiral.SpiralApiOuterClass.internal_static_proto_com_linkedin_spiral_BatchPutRequest_descriptor;
  }

  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return proto.com.linkedin.spiral.SpiralApiOuterClass.internal_static_proto_com_linkedin_spiral_BatchPutRequest_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            proto.com.linkedin.spiral.BatchPutRequest.class, proto.com.linkedin.spiral.BatchPutRequest.Builder.class);
  }

  private int bitField0_;
  public static final int SPIRALCONTEXT_FIELD_NUMBER = 1;
  private proto.com.linkedin.spiral.SpiralContext spiralContext_;
  /**
   * <code>.proto.com.linkedin.spiral.SpiralContext spiralContext = 1;</code>
   */
  public boolean hasSpiralContext() {
    return spiralContext_ != null;
  }
  /**
   * <code>.proto.com.linkedin.spiral.SpiralContext spiralContext = 1;</code>
   */
  public proto.com.linkedin.spiral.SpiralContext getSpiralContext() {
    return spiralContext_ == null ? proto.com.linkedin.spiral.SpiralContext.getDefaultInstance() : spiralContext_;
  }
  /**
   * <code>.proto.com.linkedin.spiral.SpiralContext spiralContext = 1;</code>
   */
  public proto.com.linkedin.spiral.SpiralContextOrBuilder getSpiralContextOrBuilder() {
    return getSpiralContext();
  }

  public static final int PUT_FIELD_NUMBER = 2;
  private java.util.List<proto.com.linkedin.spiral.Put> put_;
  /**
   * <code>repeated .proto.com.linkedin.spiral.Put put = 2;</code>
   */
  public java.util.List<proto.com.linkedin.spiral.Put> getPutList() {
    return put_;
  }
  /**
   * <code>repeated .proto.com.linkedin.spiral.Put put = 2;</code>
   */
  public java.util.List<? extends proto.com.linkedin.spiral.PutOrBuilder> 
      getPutOrBuilderList() {
    return put_;
  }
  /**
   * <code>repeated .proto.com.linkedin.spiral.Put put = 2;</code>
   */
  public int getPutCount() {
    return put_.size();
  }
  /**
   * <code>repeated .proto.com.linkedin.spiral.Put put = 2;</code>
   */
  public proto.com.linkedin.spiral.Put getPut(int index) {
    return put_.get(index);
  }
  /**
   * <code>repeated .proto.com.linkedin.spiral.Put put = 2;</code>
   */
  public proto.com.linkedin.spiral.PutOrBuilder getPutOrBuilder(
      int index) {
    return put_.get(index);
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
    if (spiralContext_ != null) {
      output.writeMessage(1, getSpiralContext());
    }
    for (int i = 0; i < put_.size(); i++) {
      output.writeMessage(2, put_.get(i));
    }
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (spiralContext_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, getSpiralContext());
    }
    for (int i = 0; i < put_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(2, put_.get(i));
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
    if (!(obj instanceof proto.com.linkedin.spiral.BatchPutRequest)) {
      return super.equals(obj);
    }
    proto.com.linkedin.spiral.BatchPutRequest other = (proto.com.linkedin.spiral.BatchPutRequest) obj;

    boolean result = true;
    result = result && (hasSpiralContext() == other.hasSpiralContext());
    if (hasSpiralContext()) {
      result = result && getSpiralContext()
          .equals(other.getSpiralContext());
    }
    result = result && getPutList()
        .equals(other.getPutList());
    return result;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    if (hasSpiralContext()) {
      hash = (37 * hash) + SPIRALCONTEXT_FIELD_NUMBER;
      hash = (53 * hash) + getSpiralContext().hashCode();
    }
    if (getPutCount() > 0) {
      hash = (37 * hash) + PUT_FIELD_NUMBER;
      hash = (53 * hash) + getPutList().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static proto.com.linkedin.spiral.BatchPutRequest parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static proto.com.linkedin.spiral.BatchPutRequest parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static proto.com.linkedin.spiral.BatchPutRequest parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static proto.com.linkedin.spiral.BatchPutRequest parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static proto.com.linkedin.spiral.BatchPutRequest parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static proto.com.linkedin.spiral.BatchPutRequest parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static proto.com.linkedin.spiral.BatchPutRequest parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static proto.com.linkedin.spiral.BatchPutRequest parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static proto.com.linkedin.spiral.BatchPutRequest parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static proto.com.linkedin.spiral.BatchPutRequest parseFrom(
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
  public static Builder newBuilder(proto.com.linkedin.spiral.BatchPutRequest prototype) {
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
   * Protobuf type {@code proto.com.linkedin.spiral.BatchPutRequest}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:proto.com.linkedin.spiral.BatchPutRequest)
      proto.com.linkedin.spiral.BatchPutRequestOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return proto.com.linkedin.spiral.SpiralApiOuterClass.internal_static_proto_com_linkedin_spiral_BatchPutRequest_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return proto.com.linkedin.spiral.SpiralApiOuterClass.internal_static_proto_com_linkedin_spiral_BatchPutRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              proto.com.linkedin.spiral.BatchPutRequest.class, proto.com.linkedin.spiral.BatchPutRequest.Builder.class);
    }

    // Construct using proto.com.linkedin.spiral.BatchPutRequest.newBuilder()
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
        getPutFieldBuilder();
      }
    }
    public Builder clear() {
      super.clear();
      if (spiralContextBuilder_ == null) {
        spiralContext_ = null;
      } else {
        spiralContext_ = null;
        spiralContextBuilder_ = null;
      }
      if (putBuilder_ == null) {
        put_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000002);
      } else {
        putBuilder_.clear();
      }
      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return proto.com.linkedin.spiral.SpiralApiOuterClass.internal_static_proto_com_linkedin_spiral_BatchPutRequest_descriptor;
    }

    public proto.com.linkedin.spiral.BatchPutRequest getDefaultInstanceForType() {
      return proto.com.linkedin.spiral.BatchPutRequest.getDefaultInstance();
    }

    public proto.com.linkedin.spiral.BatchPutRequest build() {
      proto.com.linkedin.spiral.BatchPutRequest result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public proto.com.linkedin.spiral.BatchPutRequest buildPartial() {
      proto.com.linkedin.spiral.BatchPutRequest result = new proto.com.linkedin.spiral.BatchPutRequest(this);
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      if (spiralContextBuilder_ == null) {
        result.spiralContext_ = spiralContext_;
      } else {
        result.spiralContext_ = spiralContextBuilder_.build();
      }
      if (putBuilder_ == null) {
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          put_ = java.util.Collections.unmodifiableList(put_);
          bitField0_ = (bitField0_ & ~0x00000002);
        }
        result.put_ = put_;
      } else {
        result.put_ = putBuilder_.build();
      }
      result.bitField0_ = to_bitField0_;
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
      if (other instanceof proto.com.linkedin.spiral.BatchPutRequest) {
        return mergeFrom((proto.com.linkedin.spiral.BatchPutRequest)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(proto.com.linkedin.spiral.BatchPutRequest other) {
      if (other == proto.com.linkedin.spiral.BatchPutRequest.getDefaultInstance()) return this;
      if (other.hasSpiralContext()) {
        mergeSpiralContext(other.getSpiralContext());
      }
      if (putBuilder_ == null) {
        if (!other.put_.isEmpty()) {
          if (put_.isEmpty()) {
            put_ = other.put_;
            bitField0_ = (bitField0_ & ~0x00000002);
          } else {
            ensurePutIsMutable();
            put_.addAll(other.put_);
          }
          onChanged();
        }
      } else {
        if (!other.put_.isEmpty()) {
          if (putBuilder_.isEmpty()) {
            putBuilder_.dispose();
            putBuilder_ = null;
            put_ = other.put_;
            bitField0_ = (bitField0_ & ~0x00000002);
            putBuilder_ = 
              com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                 getPutFieldBuilder() : null;
          } else {
            putBuilder_.addAllMessages(other.put_);
          }
        }
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
      proto.com.linkedin.spiral.BatchPutRequest parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (proto.com.linkedin.spiral.BatchPutRequest) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private proto.com.linkedin.spiral.SpiralContext spiralContext_ = null;
    private com.google.protobuf.SingleFieldBuilderV3<
        proto.com.linkedin.spiral.SpiralContext, proto.com.linkedin.spiral.SpiralContext.Builder, proto.com.linkedin.spiral.SpiralContextOrBuilder> spiralContextBuilder_;
    /**
     * <code>.proto.com.linkedin.spiral.SpiralContext spiralContext = 1;</code>
     */
    public boolean hasSpiralContext() {
      return spiralContextBuilder_ != null || spiralContext_ != null;
    }
    /**
     * <code>.proto.com.linkedin.spiral.SpiralContext spiralContext = 1;</code>
     */
    public proto.com.linkedin.spiral.SpiralContext getSpiralContext() {
      if (spiralContextBuilder_ == null) {
        return spiralContext_ == null ? proto.com.linkedin.spiral.SpiralContext.getDefaultInstance() : spiralContext_;
      } else {
        return spiralContextBuilder_.getMessage();
      }
    }
    /**
     * <code>.proto.com.linkedin.spiral.SpiralContext spiralContext = 1;</code>
     */
    public Builder setSpiralContext(proto.com.linkedin.spiral.SpiralContext value) {
      if (spiralContextBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        spiralContext_ = value;
        onChanged();
      } else {
        spiralContextBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <code>.proto.com.linkedin.spiral.SpiralContext spiralContext = 1;</code>
     */
    public Builder setSpiralContext(
        proto.com.linkedin.spiral.SpiralContext.Builder builderForValue) {
      if (spiralContextBuilder_ == null) {
        spiralContext_ = builderForValue.build();
        onChanged();
      } else {
        spiralContextBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <code>.proto.com.linkedin.spiral.SpiralContext spiralContext = 1;</code>
     */
    public Builder mergeSpiralContext(proto.com.linkedin.spiral.SpiralContext value) {
      if (spiralContextBuilder_ == null) {
        if (spiralContext_ != null) {
          spiralContext_ =
            proto.com.linkedin.spiral.SpiralContext.newBuilder(spiralContext_).mergeFrom(value).buildPartial();
        } else {
          spiralContext_ = value;
        }
        onChanged();
      } else {
        spiralContextBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <code>.proto.com.linkedin.spiral.SpiralContext spiralContext = 1;</code>
     */
    public Builder clearSpiralContext() {
      if (spiralContextBuilder_ == null) {
        spiralContext_ = null;
        onChanged();
      } else {
        spiralContext_ = null;
        spiralContextBuilder_ = null;
      }

      return this;
    }
    /**
     * <code>.proto.com.linkedin.spiral.SpiralContext spiralContext = 1;</code>
     */
    public proto.com.linkedin.spiral.SpiralContext.Builder getSpiralContextBuilder() {
      
      onChanged();
      return getSpiralContextFieldBuilder().getBuilder();
    }
    /**
     * <code>.proto.com.linkedin.spiral.SpiralContext spiralContext = 1;</code>
     */
    public proto.com.linkedin.spiral.SpiralContextOrBuilder getSpiralContextOrBuilder() {
      if (spiralContextBuilder_ != null) {
        return spiralContextBuilder_.getMessageOrBuilder();
      } else {
        return spiralContext_ == null ?
            proto.com.linkedin.spiral.SpiralContext.getDefaultInstance() : spiralContext_;
      }
    }
    /**
     * <code>.proto.com.linkedin.spiral.SpiralContext spiralContext = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        proto.com.linkedin.spiral.SpiralContext, proto.com.linkedin.spiral.SpiralContext.Builder, proto.com.linkedin.spiral.SpiralContextOrBuilder> 
        getSpiralContextFieldBuilder() {
      if (spiralContextBuilder_ == null) {
        spiralContextBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            proto.com.linkedin.spiral.SpiralContext, proto.com.linkedin.spiral.SpiralContext.Builder, proto.com.linkedin.spiral.SpiralContextOrBuilder>(
                getSpiralContext(),
                getParentForChildren(),
                isClean());
        spiralContext_ = null;
      }
      return spiralContextBuilder_;
    }

    private java.util.List<proto.com.linkedin.spiral.Put> put_ =
      java.util.Collections.emptyList();
    private void ensurePutIsMutable() {
      if (!((bitField0_ & 0x00000002) == 0x00000002)) {
        put_ = new java.util.ArrayList<proto.com.linkedin.spiral.Put>(put_);
        bitField0_ |= 0x00000002;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
        proto.com.linkedin.spiral.Put, proto.com.linkedin.spiral.Put.Builder, proto.com.linkedin.spiral.PutOrBuilder> putBuilder_;

    /**
     * <code>repeated .proto.com.linkedin.spiral.Put put = 2;</code>
     */
    public java.util.List<proto.com.linkedin.spiral.Put> getPutList() {
      if (putBuilder_ == null) {
        return java.util.Collections.unmodifiableList(put_);
      } else {
        return putBuilder_.getMessageList();
      }
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Put put = 2;</code>
     */
    public int getPutCount() {
      if (putBuilder_ == null) {
        return put_.size();
      } else {
        return putBuilder_.getCount();
      }
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Put put = 2;</code>
     */
    public proto.com.linkedin.spiral.Put getPut(int index) {
      if (putBuilder_ == null) {
        return put_.get(index);
      } else {
        return putBuilder_.getMessage(index);
      }
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Put put = 2;</code>
     */
    public Builder setPut(
        int index, proto.com.linkedin.spiral.Put value) {
      if (putBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensurePutIsMutable();
        put_.set(index, value);
        onChanged();
      } else {
        putBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Put put = 2;</code>
     */
    public Builder setPut(
        int index, proto.com.linkedin.spiral.Put.Builder builderForValue) {
      if (putBuilder_ == null) {
        ensurePutIsMutable();
        put_.set(index, builderForValue.build());
        onChanged();
      } else {
        putBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Put put = 2;</code>
     */
    public Builder addPut(proto.com.linkedin.spiral.Put value) {
      if (putBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensurePutIsMutable();
        put_.add(value);
        onChanged();
      } else {
        putBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Put put = 2;</code>
     */
    public Builder addPut(
        int index, proto.com.linkedin.spiral.Put value) {
      if (putBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensurePutIsMutable();
        put_.add(index, value);
        onChanged();
      } else {
        putBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Put put = 2;</code>
     */
    public Builder addPut(
        proto.com.linkedin.spiral.Put.Builder builderForValue) {
      if (putBuilder_ == null) {
        ensurePutIsMutable();
        put_.add(builderForValue.build());
        onChanged();
      } else {
        putBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Put put = 2;</code>
     */
    public Builder addPut(
        int index, proto.com.linkedin.spiral.Put.Builder builderForValue) {
      if (putBuilder_ == null) {
        ensurePutIsMutable();
        put_.add(index, builderForValue.build());
        onChanged();
      } else {
        putBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Put put = 2;</code>
     */
    public Builder addAllPut(
        java.lang.Iterable<? extends proto.com.linkedin.spiral.Put> values) {
      if (putBuilder_ == null) {
        ensurePutIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, put_);
        onChanged();
      } else {
        putBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Put put = 2;</code>
     */
    public Builder clearPut() {
      if (putBuilder_ == null) {
        put_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000002);
        onChanged();
      } else {
        putBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Put put = 2;</code>
     */
    public Builder removePut(int index) {
      if (putBuilder_ == null) {
        ensurePutIsMutable();
        put_.remove(index);
        onChanged();
      } else {
        putBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Put put = 2;</code>
     */
    public proto.com.linkedin.spiral.Put.Builder getPutBuilder(
        int index) {
      return getPutFieldBuilder().getBuilder(index);
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Put put = 2;</code>
     */
    public proto.com.linkedin.spiral.PutOrBuilder getPutOrBuilder(
        int index) {
      if (putBuilder_ == null) {
        return put_.get(index);  } else {
        return putBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Put put = 2;</code>
     */
    public java.util.List<? extends proto.com.linkedin.spiral.PutOrBuilder> 
         getPutOrBuilderList() {
      if (putBuilder_ != null) {
        return putBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(put_);
      }
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Put put = 2;</code>
     */
    public proto.com.linkedin.spiral.Put.Builder addPutBuilder() {
      return getPutFieldBuilder().addBuilder(
          proto.com.linkedin.spiral.Put.getDefaultInstance());
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Put put = 2;</code>
     */
    public proto.com.linkedin.spiral.Put.Builder addPutBuilder(
        int index) {
      return getPutFieldBuilder().addBuilder(
          index, proto.com.linkedin.spiral.Put.getDefaultInstance());
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Put put = 2;</code>
     */
    public java.util.List<proto.com.linkedin.spiral.Put.Builder> 
         getPutBuilderList() {
      return getPutFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilderV3<
        proto.com.linkedin.spiral.Put, proto.com.linkedin.spiral.Put.Builder, proto.com.linkedin.spiral.PutOrBuilder> 
        getPutFieldBuilder() {
      if (putBuilder_ == null) {
        putBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
            proto.com.linkedin.spiral.Put, proto.com.linkedin.spiral.Put.Builder, proto.com.linkedin.spiral.PutOrBuilder>(
                put_,
                ((bitField0_ & 0x00000002) == 0x00000002),
                getParentForChildren(),
                isClean());
        put_ = null;
      }
      return putBuilder_;
    }
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return this;
    }

    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return this;
    }


    // @@protoc_insertion_point(builder_scope:proto.com.linkedin.spiral.BatchPutRequest)
  }

  // @@protoc_insertion_point(class_scope:proto.com.linkedin.spiral.BatchPutRequest)
  private static final proto.com.linkedin.spiral.BatchPutRequest DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new proto.com.linkedin.spiral.BatchPutRequest();
  }

  public static proto.com.linkedin.spiral.BatchPutRequest getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<BatchPutRequest>
      PARSER = new com.google.protobuf.AbstractParser<BatchPutRequest>() {
    public BatchPutRequest parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return new BatchPutRequest(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<BatchPutRequest> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<BatchPutRequest> getParserForType() {
    return PARSER;
  }

  public proto.com.linkedin.spiral.BatchPutRequest getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

