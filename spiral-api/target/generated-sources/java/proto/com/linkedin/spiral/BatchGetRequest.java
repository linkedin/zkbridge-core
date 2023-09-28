// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: SpiralApi.proto

package proto.com.linkedin.spiral;

/**
 * Protobuf type {@code proto.com.linkedin.spiral.BatchGetRequest}
 */
public  final class BatchGetRequest extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:proto.com.linkedin.spiral.BatchGetRequest)
    BatchGetRequestOrBuilder {
  // Use BatchGetRequest.newBuilder() to construct.
  private BatchGetRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private BatchGetRequest() {
    keys_ = java.util.Collections.emptyList();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
  }
  private BatchGetRequest(
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
              keys_ = new java.util.ArrayList<proto.com.linkedin.spiral.Key>();
              mutable_bitField0_ |= 0x00000002;
            }
            keys_.add(
                input.readMessage(proto.com.linkedin.spiral.Key.parser(), extensionRegistry));
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
        keys_ = java.util.Collections.unmodifiableList(keys_);
      }
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return proto.com.linkedin.spiral.SpiralApiOuterClass.internal_static_proto_com_linkedin_spiral_BatchGetRequest_descriptor;
  }

  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return proto.com.linkedin.spiral.SpiralApiOuterClass.internal_static_proto_com_linkedin_spiral_BatchGetRequest_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            proto.com.linkedin.spiral.BatchGetRequest.class, proto.com.linkedin.spiral.BatchGetRequest.Builder.class);
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

  public static final int KEYS_FIELD_NUMBER = 2;
  private java.util.List<proto.com.linkedin.spiral.Key> keys_;
  /**
   * <code>repeated .proto.com.linkedin.spiral.Key keys = 2;</code>
   */
  public java.util.List<proto.com.linkedin.spiral.Key> getKeysList() {
    return keys_;
  }
  /**
   * <code>repeated .proto.com.linkedin.spiral.Key keys = 2;</code>
   */
  public java.util.List<? extends proto.com.linkedin.spiral.KeyOrBuilder> 
      getKeysOrBuilderList() {
    return keys_;
  }
  /**
   * <code>repeated .proto.com.linkedin.spiral.Key keys = 2;</code>
   */
  public int getKeysCount() {
    return keys_.size();
  }
  /**
   * <code>repeated .proto.com.linkedin.spiral.Key keys = 2;</code>
   */
  public proto.com.linkedin.spiral.Key getKeys(int index) {
    return keys_.get(index);
  }
  /**
   * <code>repeated .proto.com.linkedin.spiral.Key keys = 2;</code>
   */
  public proto.com.linkedin.spiral.KeyOrBuilder getKeysOrBuilder(
      int index) {
    return keys_.get(index);
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
    for (int i = 0; i < keys_.size(); i++) {
      output.writeMessage(2, keys_.get(i));
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
    for (int i = 0; i < keys_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(2, keys_.get(i));
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
    if (!(obj instanceof proto.com.linkedin.spiral.BatchGetRequest)) {
      return super.equals(obj);
    }
    proto.com.linkedin.spiral.BatchGetRequest other = (proto.com.linkedin.spiral.BatchGetRequest) obj;

    boolean result = true;
    result = result && (hasSpiralContext() == other.hasSpiralContext());
    if (hasSpiralContext()) {
      result = result && getSpiralContext()
          .equals(other.getSpiralContext());
    }
    result = result && getKeysList()
        .equals(other.getKeysList());
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
    if (getKeysCount() > 0) {
      hash = (37 * hash) + KEYS_FIELD_NUMBER;
      hash = (53 * hash) + getKeysList().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static proto.com.linkedin.spiral.BatchGetRequest parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static proto.com.linkedin.spiral.BatchGetRequest parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static proto.com.linkedin.spiral.BatchGetRequest parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static proto.com.linkedin.spiral.BatchGetRequest parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static proto.com.linkedin.spiral.BatchGetRequest parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static proto.com.linkedin.spiral.BatchGetRequest parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static proto.com.linkedin.spiral.BatchGetRequest parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static proto.com.linkedin.spiral.BatchGetRequest parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static proto.com.linkedin.spiral.BatchGetRequest parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static proto.com.linkedin.spiral.BatchGetRequest parseFrom(
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
  public static Builder newBuilder(proto.com.linkedin.spiral.BatchGetRequest prototype) {
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
   * Protobuf type {@code proto.com.linkedin.spiral.BatchGetRequest}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:proto.com.linkedin.spiral.BatchGetRequest)
      proto.com.linkedin.spiral.BatchGetRequestOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return proto.com.linkedin.spiral.SpiralApiOuterClass.internal_static_proto_com_linkedin_spiral_BatchGetRequest_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return proto.com.linkedin.spiral.SpiralApiOuterClass.internal_static_proto_com_linkedin_spiral_BatchGetRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              proto.com.linkedin.spiral.BatchGetRequest.class, proto.com.linkedin.spiral.BatchGetRequest.Builder.class);
    }

    // Construct using proto.com.linkedin.spiral.BatchGetRequest.newBuilder()
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
        getKeysFieldBuilder();
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
      if (keysBuilder_ == null) {
        keys_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000002);
      } else {
        keysBuilder_.clear();
      }
      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return proto.com.linkedin.spiral.SpiralApiOuterClass.internal_static_proto_com_linkedin_spiral_BatchGetRequest_descriptor;
    }

    public proto.com.linkedin.spiral.BatchGetRequest getDefaultInstanceForType() {
      return proto.com.linkedin.spiral.BatchGetRequest.getDefaultInstance();
    }

    public proto.com.linkedin.spiral.BatchGetRequest build() {
      proto.com.linkedin.spiral.BatchGetRequest result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public proto.com.linkedin.spiral.BatchGetRequest buildPartial() {
      proto.com.linkedin.spiral.BatchGetRequest result = new proto.com.linkedin.spiral.BatchGetRequest(this);
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      if (spiralContextBuilder_ == null) {
        result.spiralContext_ = spiralContext_;
      } else {
        result.spiralContext_ = spiralContextBuilder_.build();
      }
      if (keysBuilder_ == null) {
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          keys_ = java.util.Collections.unmodifiableList(keys_);
          bitField0_ = (bitField0_ & ~0x00000002);
        }
        result.keys_ = keys_;
      } else {
        result.keys_ = keysBuilder_.build();
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
      if (other instanceof proto.com.linkedin.spiral.BatchGetRequest) {
        return mergeFrom((proto.com.linkedin.spiral.BatchGetRequest)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(proto.com.linkedin.spiral.BatchGetRequest other) {
      if (other == proto.com.linkedin.spiral.BatchGetRequest.getDefaultInstance()) return this;
      if (other.hasSpiralContext()) {
        mergeSpiralContext(other.getSpiralContext());
      }
      if (keysBuilder_ == null) {
        if (!other.keys_.isEmpty()) {
          if (keys_.isEmpty()) {
            keys_ = other.keys_;
            bitField0_ = (bitField0_ & ~0x00000002);
          } else {
            ensureKeysIsMutable();
            keys_.addAll(other.keys_);
          }
          onChanged();
        }
      } else {
        if (!other.keys_.isEmpty()) {
          if (keysBuilder_.isEmpty()) {
            keysBuilder_.dispose();
            keysBuilder_ = null;
            keys_ = other.keys_;
            bitField0_ = (bitField0_ & ~0x00000002);
            keysBuilder_ = 
              com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                 getKeysFieldBuilder() : null;
          } else {
            keysBuilder_.addAllMessages(other.keys_);
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
      proto.com.linkedin.spiral.BatchGetRequest parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (proto.com.linkedin.spiral.BatchGetRequest) e.getUnfinishedMessage();
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

    private java.util.List<proto.com.linkedin.spiral.Key> keys_ =
      java.util.Collections.emptyList();
    private void ensureKeysIsMutable() {
      if (!((bitField0_ & 0x00000002) == 0x00000002)) {
        keys_ = new java.util.ArrayList<proto.com.linkedin.spiral.Key>(keys_);
        bitField0_ |= 0x00000002;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
        proto.com.linkedin.spiral.Key, proto.com.linkedin.spiral.Key.Builder, proto.com.linkedin.spiral.KeyOrBuilder> keysBuilder_;

    /**
     * <code>repeated .proto.com.linkedin.spiral.Key keys = 2;</code>
     */
    public java.util.List<proto.com.linkedin.spiral.Key> getKeysList() {
      if (keysBuilder_ == null) {
        return java.util.Collections.unmodifiableList(keys_);
      } else {
        return keysBuilder_.getMessageList();
      }
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Key keys = 2;</code>
     */
    public int getKeysCount() {
      if (keysBuilder_ == null) {
        return keys_.size();
      } else {
        return keysBuilder_.getCount();
      }
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Key keys = 2;</code>
     */
    public proto.com.linkedin.spiral.Key getKeys(int index) {
      if (keysBuilder_ == null) {
        return keys_.get(index);
      } else {
        return keysBuilder_.getMessage(index);
      }
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Key keys = 2;</code>
     */
    public Builder setKeys(
        int index, proto.com.linkedin.spiral.Key value) {
      if (keysBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureKeysIsMutable();
        keys_.set(index, value);
        onChanged();
      } else {
        keysBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Key keys = 2;</code>
     */
    public Builder setKeys(
        int index, proto.com.linkedin.spiral.Key.Builder builderForValue) {
      if (keysBuilder_ == null) {
        ensureKeysIsMutable();
        keys_.set(index, builderForValue.build());
        onChanged();
      } else {
        keysBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Key keys = 2;</code>
     */
    public Builder addKeys(proto.com.linkedin.spiral.Key value) {
      if (keysBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureKeysIsMutable();
        keys_.add(value);
        onChanged();
      } else {
        keysBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Key keys = 2;</code>
     */
    public Builder addKeys(
        int index, proto.com.linkedin.spiral.Key value) {
      if (keysBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureKeysIsMutable();
        keys_.add(index, value);
        onChanged();
      } else {
        keysBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Key keys = 2;</code>
     */
    public Builder addKeys(
        proto.com.linkedin.spiral.Key.Builder builderForValue) {
      if (keysBuilder_ == null) {
        ensureKeysIsMutable();
        keys_.add(builderForValue.build());
        onChanged();
      } else {
        keysBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Key keys = 2;</code>
     */
    public Builder addKeys(
        int index, proto.com.linkedin.spiral.Key.Builder builderForValue) {
      if (keysBuilder_ == null) {
        ensureKeysIsMutable();
        keys_.add(index, builderForValue.build());
        onChanged();
      } else {
        keysBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Key keys = 2;</code>
     */
    public Builder addAllKeys(
        java.lang.Iterable<? extends proto.com.linkedin.spiral.Key> values) {
      if (keysBuilder_ == null) {
        ensureKeysIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, keys_);
        onChanged();
      } else {
        keysBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Key keys = 2;</code>
     */
    public Builder clearKeys() {
      if (keysBuilder_ == null) {
        keys_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000002);
        onChanged();
      } else {
        keysBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Key keys = 2;</code>
     */
    public Builder removeKeys(int index) {
      if (keysBuilder_ == null) {
        ensureKeysIsMutable();
        keys_.remove(index);
        onChanged();
      } else {
        keysBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Key keys = 2;</code>
     */
    public proto.com.linkedin.spiral.Key.Builder getKeysBuilder(
        int index) {
      return getKeysFieldBuilder().getBuilder(index);
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Key keys = 2;</code>
     */
    public proto.com.linkedin.spiral.KeyOrBuilder getKeysOrBuilder(
        int index) {
      if (keysBuilder_ == null) {
        return keys_.get(index);  } else {
        return keysBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Key keys = 2;</code>
     */
    public java.util.List<? extends proto.com.linkedin.spiral.KeyOrBuilder> 
         getKeysOrBuilderList() {
      if (keysBuilder_ != null) {
        return keysBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(keys_);
      }
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Key keys = 2;</code>
     */
    public proto.com.linkedin.spiral.Key.Builder addKeysBuilder() {
      return getKeysFieldBuilder().addBuilder(
          proto.com.linkedin.spiral.Key.getDefaultInstance());
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Key keys = 2;</code>
     */
    public proto.com.linkedin.spiral.Key.Builder addKeysBuilder(
        int index) {
      return getKeysFieldBuilder().addBuilder(
          index, proto.com.linkedin.spiral.Key.getDefaultInstance());
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Key keys = 2;</code>
     */
    public java.util.List<proto.com.linkedin.spiral.Key.Builder> 
         getKeysBuilderList() {
      return getKeysFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilderV3<
        proto.com.linkedin.spiral.Key, proto.com.linkedin.spiral.Key.Builder, proto.com.linkedin.spiral.KeyOrBuilder> 
        getKeysFieldBuilder() {
      if (keysBuilder_ == null) {
        keysBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
            proto.com.linkedin.spiral.Key, proto.com.linkedin.spiral.Key.Builder, proto.com.linkedin.spiral.KeyOrBuilder>(
                keys_,
                ((bitField0_ & 0x00000002) == 0x00000002),
                getParentForChildren(),
                isClean());
        keys_ = null;
      }
      return keysBuilder_;
    }
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return this;
    }

    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return this;
    }


    // @@protoc_insertion_point(builder_scope:proto.com.linkedin.spiral.BatchGetRequest)
  }

  // @@protoc_insertion_point(class_scope:proto.com.linkedin.spiral.BatchGetRequest)
  private static final proto.com.linkedin.spiral.BatchGetRequest DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new proto.com.linkedin.spiral.BatchGetRequest();
  }

  public static proto.com.linkedin.spiral.BatchGetRequest getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<BatchGetRequest>
      PARSER = new com.google.protobuf.AbstractParser<BatchGetRequest>() {
    public BatchGetRequest parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return new BatchGetRequest(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<BatchGetRequest> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<BatchGetRequest> getParserForType() {
    return PARSER;
  }

  public proto.com.linkedin.spiral.BatchGetRequest getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

