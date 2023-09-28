// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: SpiralApi.proto

package proto.com.linkedin.spiral;

/**
 * Protobuf type {@code proto.com.linkedin.spiral.GetRequest}
 */
public  final class GetRequest extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:proto.com.linkedin.spiral.GetRequest)
    GetRequestOrBuilder {
  // Use GetRequest.newBuilder() to construct.
  private GetRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private GetRequest() {
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
  }
  private GetRequest(
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
            proto.com.linkedin.spiral.Key.Builder subBuilder = null;
            if (key_ != null) {
              subBuilder = key_.toBuilder();
            }
            key_ = input.readMessage(proto.com.linkedin.spiral.Key.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(key_);
              key_ = subBuilder.buildPartial();
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
    return proto.com.linkedin.spiral.SpiralApiOuterClass.internal_static_proto_com_linkedin_spiral_GetRequest_descriptor;
  }

  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return proto.com.linkedin.spiral.SpiralApiOuterClass.internal_static_proto_com_linkedin_spiral_GetRequest_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            proto.com.linkedin.spiral.GetRequest.class, proto.com.linkedin.spiral.GetRequest.Builder.class);
  }

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

  public static final int KEY_FIELD_NUMBER = 2;
  private proto.com.linkedin.spiral.Key key_;
  /**
   * <code>.proto.com.linkedin.spiral.Key key = 2;</code>
   */
  public boolean hasKey() {
    return key_ != null;
  }
  /**
   * <code>.proto.com.linkedin.spiral.Key key = 2;</code>
   */
  public proto.com.linkedin.spiral.Key getKey() {
    return key_ == null ? proto.com.linkedin.spiral.Key.getDefaultInstance() : key_;
  }
  /**
   * <code>.proto.com.linkedin.spiral.Key key = 2;</code>
   */
  public proto.com.linkedin.spiral.KeyOrBuilder getKeyOrBuilder() {
    return getKey();
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
    if (key_ != null) {
      output.writeMessage(2, getKey());
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
    if (key_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(2, getKey());
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
    if (!(obj instanceof proto.com.linkedin.spiral.GetRequest)) {
      return super.equals(obj);
    }
    proto.com.linkedin.spiral.GetRequest other = (proto.com.linkedin.spiral.GetRequest) obj;

    boolean result = true;
    result = result && (hasSpiralContext() == other.hasSpiralContext());
    if (hasSpiralContext()) {
      result = result && getSpiralContext()
          .equals(other.getSpiralContext());
    }
    result = result && (hasKey() == other.hasKey());
    if (hasKey()) {
      result = result && getKey()
          .equals(other.getKey());
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
    if (hasSpiralContext()) {
      hash = (37 * hash) + SPIRALCONTEXT_FIELD_NUMBER;
      hash = (53 * hash) + getSpiralContext().hashCode();
    }
    if (hasKey()) {
      hash = (37 * hash) + KEY_FIELD_NUMBER;
      hash = (53 * hash) + getKey().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static proto.com.linkedin.spiral.GetRequest parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static proto.com.linkedin.spiral.GetRequest parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static proto.com.linkedin.spiral.GetRequest parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static proto.com.linkedin.spiral.GetRequest parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static proto.com.linkedin.spiral.GetRequest parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static proto.com.linkedin.spiral.GetRequest parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static proto.com.linkedin.spiral.GetRequest parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static proto.com.linkedin.spiral.GetRequest parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static proto.com.linkedin.spiral.GetRequest parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static proto.com.linkedin.spiral.GetRequest parseFrom(
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
  public static Builder newBuilder(proto.com.linkedin.spiral.GetRequest prototype) {
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
   * Protobuf type {@code proto.com.linkedin.spiral.GetRequest}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:proto.com.linkedin.spiral.GetRequest)
      proto.com.linkedin.spiral.GetRequestOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return proto.com.linkedin.spiral.SpiralApiOuterClass.internal_static_proto_com_linkedin_spiral_GetRequest_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return proto.com.linkedin.spiral.SpiralApiOuterClass.internal_static_proto_com_linkedin_spiral_GetRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              proto.com.linkedin.spiral.GetRequest.class, proto.com.linkedin.spiral.GetRequest.Builder.class);
    }

    // Construct using proto.com.linkedin.spiral.GetRequest.newBuilder()
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
      if (spiralContextBuilder_ == null) {
        spiralContext_ = null;
      } else {
        spiralContext_ = null;
        spiralContextBuilder_ = null;
      }
      if (keyBuilder_ == null) {
        key_ = null;
      } else {
        key_ = null;
        keyBuilder_ = null;
      }
      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return proto.com.linkedin.spiral.SpiralApiOuterClass.internal_static_proto_com_linkedin_spiral_GetRequest_descriptor;
    }

    public proto.com.linkedin.spiral.GetRequest getDefaultInstanceForType() {
      return proto.com.linkedin.spiral.GetRequest.getDefaultInstance();
    }

    public proto.com.linkedin.spiral.GetRequest build() {
      proto.com.linkedin.spiral.GetRequest result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public proto.com.linkedin.spiral.GetRequest buildPartial() {
      proto.com.linkedin.spiral.GetRequest result = new proto.com.linkedin.spiral.GetRequest(this);
      if (spiralContextBuilder_ == null) {
        result.spiralContext_ = spiralContext_;
      } else {
        result.spiralContext_ = spiralContextBuilder_.build();
      }
      if (keyBuilder_ == null) {
        result.key_ = key_;
      } else {
        result.key_ = keyBuilder_.build();
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
      if (other instanceof proto.com.linkedin.spiral.GetRequest) {
        return mergeFrom((proto.com.linkedin.spiral.GetRequest)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(proto.com.linkedin.spiral.GetRequest other) {
      if (other == proto.com.linkedin.spiral.GetRequest.getDefaultInstance()) return this;
      if (other.hasSpiralContext()) {
        mergeSpiralContext(other.getSpiralContext());
      }
      if (other.hasKey()) {
        mergeKey(other.getKey());
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
      proto.com.linkedin.spiral.GetRequest parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (proto.com.linkedin.spiral.GetRequest) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

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

    private proto.com.linkedin.spiral.Key key_ = null;
    private com.google.protobuf.SingleFieldBuilderV3<
        proto.com.linkedin.spiral.Key, proto.com.linkedin.spiral.Key.Builder, proto.com.linkedin.spiral.KeyOrBuilder> keyBuilder_;
    /**
     * <code>.proto.com.linkedin.spiral.Key key = 2;</code>
     */
    public boolean hasKey() {
      return keyBuilder_ != null || key_ != null;
    }
    /**
     * <code>.proto.com.linkedin.spiral.Key key = 2;</code>
     */
    public proto.com.linkedin.spiral.Key getKey() {
      if (keyBuilder_ == null) {
        return key_ == null ? proto.com.linkedin.spiral.Key.getDefaultInstance() : key_;
      } else {
        return keyBuilder_.getMessage();
      }
    }
    /**
     * <code>.proto.com.linkedin.spiral.Key key = 2;</code>
     */
    public Builder setKey(proto.com.linkedin.spiral.Key value) {
      if (keyBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        key_ = value;
        onChanged();
      } else {
        keyBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <code>.proto.com.linkedin.spiral.Key key = 2;</code>
     */
    public Builder setKey(
        proto.com.linkedin.spiral.Key.Builder builderForValue) {
      if (keyBuilder_ == null) {
        key_ = builderForValue.build();
        onChanged();
      } else {
        keyBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <code>.proto.com.linkedin.spiral.Key key = 2;</code>
     */
    public Builder mergeKey(proto.com.linkedin.spiral.Key value) {
      if (keyBuilder_ == null) {
        if (key_ != null) {
          key_ =
            proto.com.linkedin.spiral.Key.newBuilder(key_).mergeFrom(value).buildPartial();
        } else {
          key_ = value;
        }
        onChanged();
      } else {
        keyBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <code>.proto.com.linkedin.spiral.Key key = 2;</code>
     */
    public Builder clearKey() {
      if (keyBuilder_ == null) {
        key_ = null;
        onChanged();
      } else {
        key_ = null;
        keyBuilder_ = null;
      }

      return this;
    }
    /**
     * <code>.proto.com.linkedin.spiral.Key key = 2;</code>
     */
    public proto.com.linkedin.spiral.Key.Builder getKeyBuilder() {
      
      onChanged();
      return getKeyFieldBuilder().getBuilder();
    }
    /**
     * <code>.proto.com.linkedin.spiral.Key key = 2;</code>
     */
    public proto.com.linkedin.spiral.KeyOrBuilder getKeyOrBuilder() {
      if (keyBuilder_ != null) {
        return keyBuilder_.getMessageOrBuilder();
      } else {
        return key_ == null ?
            proto.com.linkedin.spiral.Key.getDefaultInstance() : key_;
      }
    }
    /**
     * <code>.proto.com.linkedin.spiral.Key key = 2;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        proto.com.linkedin.spiral.Key, proto.com.linkedin.spiral.Key.Builder, proto.com.linkedin.spiral.KeyOrBuilder> 
        getKeyFieldBuilder() {
      if (keyBuilder_ == null) {
        keyBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            proto.com.linkedin.spiral.Key, proto.com.linkedin.spiral.Key.Builder, proto.com.linkedin.spiral.KeyOrBuilder>(
                getKey(),
                getParentForChildren(),
                isClean());
        key_ = null;
      }
      return keyBuilder_;
    }
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return this;
    }

    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return this;
    }


    // @@protoc_insertion_point(builder_scope:proto.com.linkedin.spiral.GetRequest)
  }

  // @@protoc_insertion_point(class_scope:proto.com.linkedin.spiral.GetRequest)
  private static final proto.com.linkedin.spiral.GetRequest DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new proto.com.linkedin.spiral.GetRequest();
  }

  public static proto.com.linkedin.spiral.GetRequest getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<GetRequest>
      PARSER = new com.google.protobuf.AbstractParser<GetRequest>() {
    public GetRequest parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return new GetRequest(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<GetRequest> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<GetRequest> getParserForType() {
    return PARSER;
  }

  public proto.com.linkedin.spiral.GetRequest getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

