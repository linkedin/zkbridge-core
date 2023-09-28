// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: SpiralApi.proto

package proto.com.linkedin.spiral;

/**
 * Protobuf type {@code proto.com.linkedin.spiral.ScanResponse}
 */
public  final class ScanResponse extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:proto.com.linkedin.spiral.ScanResponse)
    ScanResponseOrBuilder {
  // Use ScanResponse.newBuilder() to construct.
  private ScanResponse(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private ScanResponse() {
    keyValues_ = java.util.Collections.emptyList();
    nextPaginationToken_ = "";
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
  }
  private ScanResponse(
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
            if (!((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
              keyValues_ = new java.util.ArrayList<proto.com.linkedin.spiral.KeyValue>();
              mutable_bitField0_ |= 0x00000001;
            }
            keyValues_.add(
                input.readMessage(proto.com.linkedin.spiral.KeyValue.parser(), extensionRegistry));
            break;
          }
          case 18: {
            java.lang.String s = input.readStringRequireUtf8();

            nextPaginationToken_ = s;
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
      if (((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
        keyValues_ = java.util.Collections.unmodifiableList(keyValues_);
      }
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return proto.com.linkedin.spiral.SpiralApiOuterClass.internal_static_proto_com_linkedin_spiral_ScanResponse_descriptor;
  }

  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return proto.com.linkedin.spiral.SpiralApiOuterClass.internal_static_proto_com_linkedin_spiral_ScanResponse_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            proto.com.linkedin.spiral.ScanResponse.class, proto.com.linkedin.spiral.ScanResponse.Builder.class);
  }

  private int bitField0_;
  public static final int KEYVALUES_FIELD_NUMBER = 1;
  private java.util.List<proto.com.linkedin.spiral.KeyValue> keyValues_;
  /**
   * <code>repeated .proto.com.linkedin.spiral.KeyValue keyValues = 1;</code>
   */
  public java.util.List<proto.com.linkedin.spiral.KeyValue> getKeyValuesList() {
    return keyValues_;
  }
  /**
   * <code>repeated .proto.com.linkedin.spiral.KeyValue keyValues = 1;</code>
   */
  public java.util.List<? extends proto.com.linkedin.spiral.KeyValueOrBuilder> 
      getKeyValuesOrBuilderList() {
    return keyValues_;
  }
  /**
   * <code>repeated .proto.com.linkedin.spiral.KeyValue keyValues = 1;</code>
   */
  public int getKeyValuesCount() {
    return keyValues_.size();
  }
  /**
   * <code>repeated .proto.com.linkedin.spiral.KeyValue keyValues = 1;</code>
   */
  public proto.com.linkedin.spiral.KeyValue getKeyValues(int index) {
    return keyValues_.get(index);
  }
  /**
   * <code>repeated .proto.com.linkedin.spiral.KeyValue keyValues = 1;</code>
   */
  public proto.com.linkedin.spiral.KeyValueOrBuilder getKeyValuesOrBuilder(
      int index) {
    return keyValues_.get(index);
  }

  public static final int NEXTPAGINATIONTOKEN_FIELD_NUMBER = 2;
  private volatile java.lang.Object nextPaginationToken_;
  /**
   * <code>string nextPaginationToken = 2;</code>
   */
  public java.lang.String getNextPaginationToken() {
    java.lang.Object ref = nextPaginationToken_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      nextPaginationToken_ = s;
      return s;
    }
  }
  /**
   * <code>string nextPaginationToken = 2;</code>
   */
  public com.google.protobuf.ByteString
      getNextPaginationTokenBytes() {
    java.lang.Object ref = nextPaginationToken_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      nextPaginationToken_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
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
    for (int i = 0; i < keyValues_.size(); i++) {
      output.writeMessage(1, keyValues_.get(i));
    }
    if (!getNextPaginationTokenBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, nextPaginationToken_);
    }
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    for (int i = 0; i < keyValues_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, keyValues_.get(i));
    }
    if (!getNextPaginationTokenBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, nextPaginationToken_);
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
    if (!(obj instanceof proto.com.linkedin.spiral.ScanResponse)) {
      return super.equals(obj);
    }
    proto.com.linkedin.spiral.ScanResponse other = (proto.com.linkedin.spiral.ScanResponse) obj;

    boolean result = true;
    result = result && getKeyValuesList()
        .equals(other.getKeyValuesList());
    result = result && getNextPaginationToken()
        .equals(other.getNextPaginationToken());
    return result;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    if (getKeyValuesCount() > 0) {
      hash = (37 * hash) + KEYVALUES_FIELD_NUMBER;
      hash = (53 * hash) + getKeyValuesList().hashCode();
    }
    hash = (37 * hash) + NEXTPAGINATIONTOKEN_FIELD_NUMBER;
    hash = (53 * hash) + getNextPaginationToken().hashCode();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static proto.com.linkedin.spiral.ScanResponse parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static proto.com.linkedin.spiral.ScanResponse parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static proto.com.linkedin.spiral.ScanResponse parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static proto.com.linkedin.spiral.ScanResponse parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static proto.com.linkedin.spiral.ScanResponse parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static proto.com.linkedin.spiral.ScanResponse parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static proto.com.linkedin.spiral.ScanResponse parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static proto.com.linkedin.spiral.ScanResponse parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static proto.com.linkedin.spiral.ScanResponse parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static proto.com.linkedin.spiral.ScanResponse parseFrom(
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
  public static Builder newBuilder(proto.com.linkedin.spiral.ScanResponse prototype) {
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
   * Protobuf type {@code proto.com.linkedin.spiral.ScanResponse}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:proto.com.linkedin.spiral.ScanResponse)
      proto.com.linkedin.spiral.ScanResponseOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return proto.com.linkedin.spiral.SpiralApiOuterClass.internal_static_proto_com_linkedin_spiral_ScanResponse_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return proto.com.linkedin.spiral.SpiralApiOuterClass.internal_static_proto_com_linkedin_spiral_ScanResponse_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              proto.com.linkedin.spiral.ScanResponse.class, proto.com.linkedin.spiral.ScanResponse.Builder.class);
    }

    // Construct using proto.com.linkedin.spiral.ScanResponse.newBuilder()
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
        getKeyValuesFieldBuilder();
      }
    }
    public Builder clear() {
      super.clear();
      if (keyValuesBuilder_ == null) {
        keyValues_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
      } else {
        keyValuesBuilder_.clear();
      }
      nextPaginationToken_ = "";

      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return proto.com.linkedin.spiral.SpiralApiOuterClass.internal_static_proto_com_linkedin_spiral_ScanResponse_descriptor;
    }

    public proto.com.linkedin.spiral.ScanResponse getDefaultInstanceForType() {
      return proto.com.linkedin.spiral.ScanResponse.getDefaultInstance();
    }

    public proto.com.linkedin.spiral.ScanResponse build() {
      proto.com.linkedin.spiral.ScanResponse result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public proto.com.linkedin.spiral.ScanResponse buildPartial() {
      proto.com.linkedin.spiral.ScanResponse result = new proto.com.linkedin.spiral.ScanResponse(this);
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      if (keyValuesBuilder_ == null) {
        if (((bitField0_ & 0x00000001) == 0x00000001)) {
          keyValues_ = java.util.Collections.unmodifiableList(keyValues_);
          bitField0_ = (bitField0_ & ~0x00000001);
        }
        result.keyValues_ = keyValues_;
      } else {
        result.keyValues_ = keyValuesBuilder_.build();
      }
      result.nextPaginationToken_ = nextPaginationToken_;
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
      if (other instanceof proto.com.linkedin.spiral.ScanResponse) {
        return mergeFrom((proto.com.linkedin.spiral.ScanResponse)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(proto.com.linkedin.spiral.ScanResponse other) {
      if (other == proto.com.linkedin.spiral.ScanResponse.getDefaultInstance()) return this;
      if (keyValuesBuilder_ == null) {
        if (!other.keyValues_.isEmpty()) {
          if (keyValues_.isEmpty()) {
            keyValues_ = other.keyValues_;
            bitField0_ = (bitField0_ & ~0x00000001);
          } else {
            ensureKeyValuesIsMutable();
            keyValues_.addAll(other.keyValues_);
          }
          onChanged();
        }
      } else {
        if (!other.keyValues_.isEmpty()) {
          if (keyValuesBuilder_.isEmpty()) {
            keyValuesBuilder_.dispose();
            keyValuesBuilder_ = null;
            keyValues_ = other.keyValues_;
            bitField0_ = (bitField0_ & ~0x00000001);
            keyValuesBuilder_ = 
              com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                 getKeyValuesFieldBuilder() : null;
          } else {
            keyValuesBuilder_.addAllMessages(other.keyValues_);
          }
        }
      }
      if (!other.getNextPaginationToken().isEmpty()) {
        nextPaginationToken_ = other.nextPaginationToken_;
        onChanged();
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
      proto.com.linkedin.spiral.ScanResponse parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (proto.com.linkedin.spiral.ScanResponse) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private java.util.List<proto.com.linkedin.spiral.KeyValue> keyValues_ =
      java.util.Collections.emptyList();
    private void ensureKeyValuesIsMutable() {
      if (!((bitField0_ & 0x00000001) == 0x00000001)) {
        keyValues_ = new java.util.ArrayList<proto.com.linkedin.spiral.KeyValue>(keyValues_);
        bitField0_ |= 0x00000001;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
        proto.com.linkedin.spiral.KeyValue, proto.com.linkedin.spiral.KeyValue.Builder, proto.com.linkedin.spiral.KeyValueOrBuilder> keyValuesBuilder_;

    /**
     * <code>repeated .proto.com.linkedin.spiral.KeyValue keyValues = 1;</code>
     */
    public java.util.List<proto.com.linkedin.spiral.KeyValue> getKeyValuesList() {
      if (keyValuesBuilder_ == null) {
        return java.util.Collections.unmodifiableList(keyValues_);
      } else {
        return keyValuesBuilder_.getMessageList();
      }
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.KeyValue keyValues = 1;</code>
     */
    public int getKeyValuesCount() {
      if (keyValuesBuilder_ == null) {
        return keyValues_.size();
      } else {
        return keyValuesBuilder_.getCount();
      }
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.KeyValue keyValues = 1;</code>
     */
    public proto.com.linkedin.spiral.KeyValue getKeyValues(int index) {
      if (keyValuesBuilder_ == null) {
        return keyValues_.get(index);
      } else {
        return keyValuesBuilder_.getMessage(index);
      }
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.KeyValue keyValues = 1;</code>
     */
    public Builder setKeyValues(
        int index, proto.com.linkedin.spiral.KeyValue value) {
      if (keyValuesBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureKeyValuesIsMutable();
        keyValues_.set(index, value);
        onChanged();
      } else {
        keyValuesBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.KeyValue keyValues = 1;</code>
     */
    public Builder setKeyValues(
        int index, proto.com.linkedin.spiral.KeyValue.Builder builderForValue) {
      if (keyValuesBuilder_ == null) {
        ensureKeyValuesIsMutable();
        keyValues_.set(index, builderForValue.build());
        onChanged();
      } else {
        keyValuesBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.KeyValue keyValues = 1;</code>
     */
    public Builder addKeyValues(proto.com.linkedin.spiral.KeyValue value) {
      if (keyValuesBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureKeyValuesIsMutable();
        keyValues_.add(value);
        onChanged();
      } else {
        keyValuesBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.KeyValue keyValues = 1;</code>
     */
    public Builder addKeyValues(
        int index, proto.com.linkedin.spiral.KeyValue value) {
      if (keyValuesBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureKeyValuesIsMutable();
        keyValues_.add(index, value);
        onChanged();
      } else {
        keyValuesBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.KeyValue keyValues = 1;</code>
     */
    public Builder addKeyValues(
        proto.com.linkedin.spiral.KeyValue.Builder builderForValue) {
      if (keyValuesBuilder_ == null) {
        ensureKeyValuesIsMutable();
        keyValues_.add(builderForValue.build());
        onChanged();
      } else {
        keyValuesBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.KeyValue keyValues = 1;</code>
     */
    public Builder addKeyValues(
        int index, proto.com.linkedin.spiral.KeyValue.Builder builderForValue) {
      if (keyValuesBuilder_ == null) {
        ensureKeyValuesIsMutable();
        keyValues_.add(index, builderForValue.build());
        onChanged();
      } else {
        keyValuesBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.KeyValue keyValues = 1;</code>
     */
    public Builder addAllKeyValues(
        java.lang.Iterable<? extends proto.com.linkedin.spiral.KeyValue> values) {
      if (keyValuesBuilder_ == null) {
        ensureKeyValuesIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, keyValues_);
        onChanged();
      } else {
        keyValuesBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.KeyValue keyValues = 1;</code>
     */
    public Builder clearKeyValues() {
      if (keyValuesBuilder_ == null) {
        keyValues_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
      } else {
        keyValuesBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.KeyValue keyValues = 1;</code>
     */
    public Builder removeKeyValues(int index) {
      if (keyValuesBuilder_ == null) {
        ensureKeyValuesIsMutable();
        keyValues_.remove(index);
        onChanged();
      } else {
        keyValuesBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.KeyValue keyValues = 1;</code>
     */
    public proto.com.linkedin.spiral.KeyValue.Builder getKeyValuesBuilder(
        int index) {
      return getKeyValuesFieldBuilder().getBuilder(index);
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.KeyValue keyValues = 1;</code>
     */
    public proto.com.linkedin.spiral.KeyValueOrBuilder getKeyValuesOrBuilder(
        int index) {
      if (keyValuesBuilder_ == null) {
        return keyValues_.get(index);  } else {
        return keyValuesBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.KeyValue keyValues = 1;</code>
     */
    public java.util.List<? extends proto.com.linkedin.spiral.KeyValueOrBuilder> 
         getKeyValuesOrBuilderList() {
      if (keyValuesBuilder_ != null) {
        return keyValuesBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(keyValues_);
      }
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.KeyValue keyValues = 1;</code>
     */
    public proto.com.linkedin.spiral.KeyValue.Builder addKeyValuesBuilder() {
      return getKeyValuesFieldBuilder().addBuilder(
          proto.com.linkedin.spiral.KeyValue.getDefaultInstance());
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.KeyValue keyValues = 1;</code>
     */
    public proto.com.linkedin.spiral.KeyValue.Builder addKeyValuesBuilder(
        int index) {
      return getKeyValuesFieldBuilder().addBuilder(
          index, proto.com.linkedin.spiral.KeyValue.getDefaultInstance());
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.KeyValue keyValues = 1;</code>
     */
    public java.util.List<proto.com.linkedin.spiral.KeyValue.Builder> 
         getKeyValuesBuilderList() {
      return getKeyValuesFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilderV3<
        proto.com.linkedin.spiral.KeyValue, proto.com.linkedin.spiral.KeyValue.Builder, proto.com.linkedin.spiral.KeyValueOrBuilder> 
        getKeyValuesFieldBuilder() {
      if (keyValuesBuilder_ == null) {
        keyValuesBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
            proto.com.linkedin.spiral.KeyValue, proto.com.linkedin.spiral.KeyValue.Builder, proto.com.linkedin.spiral.KeyValueOrBuilder>(
                keyValues_,
                ((bitField0_ & 0x00000001) == 0x00000001),
                getParentForChildren(),
                isClean());
        keyValues_ = null;
      }
      return keyValuesBuilder_;
    }

    private java.lang.Object nextPaginationToken_ = "";
    /**
     * <code>string nextPaginationToken = 2;</code>
     */
    public java.lang.String getNextPaginationToken() {
      java.lang.Object ref = nextPaginationToken_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        nextPaginationToken_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string nextPaginationToken = 2;</code>
     */
    public com.google.protobuf.ByteString
        getNextPaginationTokenBytes() {
      java.lang.Object ref = nextPaginationToken_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        nextPaginationToken_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string nextPaginationToken = 2;</code>
     */
    public Builder setNextPaginationToken(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      nextPaginationToken_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string nextPaginationToken = 2;</code>
     */
    public Builder clearNextPaginationToken() {
      
      nextPaginationToken_ = getDefaultInstance().getNextPaginationToken();
      onChanged();
      return this;
    }
    /**
     * <code>string nextPaginationToken = 2;</code>
     */
    public Builder setNextPaginationTokenBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      nextPaginationToken_ = value;
      onChanged();
      return this;
    }
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return this;
    }

    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return this;
    }


    // @@protoc_insertion_point(builder_scope:proto.com.linkedin.spiral.ScanResponse)
  }

  // @@protoc_insertion_point(class_scope:proto.com.linkedin.spiral.ScanResponse)
  private static final proto.com.linkedin.spiral.ScanResponse DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new proto.com.linkedin.spiral.ScanResponse();
  }

  public static proto.com.linkedin.spiral.ScanResponse getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<ScanResponse>
      PARSER = new com.google.protobuf.AbstractParser<ScanResponse>() {
    public ScanResponse parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return new ScanResponse(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<ScanResponse> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<ScanResponse> getParserForType() {
    return PARSER;
  }

  public proto.com.linkedin.spiral.ScanResponse getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

