// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: SpiralApi.proto

package proto.com.linkedin.spiral;

/**
 * Protobuf type {@code proto.com.linkedin.spiral.GetNamespaceResponse}
 */
public  final class GetNamespaceResponse extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:proto.com.linkedin.spiral.GetNamespaceResponse)
    GetNamespaceResponseOrBuilder {
  // Use GetNamespaceResponse.newBuilder() to construct.
  private GetNamespaceResponse(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private GetNamespaceResponse() {
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
  }
  private GetNamespaceResponse(
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
            proto.com.linkedin.spiral.Namespace.Builder subBuilder = null;
            if (namespace_ != null) {
              subBuilder = namespace_.toBuilder();
            }
            namespace_ = input.readMessage(proto.com.linkedin.spiral.Namespace.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(namespace_);
              namespace_ = subBuilder.buildPartial();
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
    return proto.com.linkedin.spiral.SpiralApiOuterClass.internal_static_proto_com_linkedin_spiral_GetNamespaceResponse_descriptor;
  }

  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return proto.com.linkedin.spiral.SpiralApiOuterClass.internal_static_proto_com_linkedin_spiral_GetNamespaceResponse_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            proto.com.linkedin.spiral.GetNamespaceResponse.class, proto.com.linkedin.spiral.GetNamespaceResponse.Builder.class);
  }

  public static final int NAMESPACE_FIELD_NUMBER = 1;
  private proto.com.linkedin.spiral.Namespace namespace_;
  /**
   * <code>.proto.com.linkedin.spiral.Namespace namespace = 1;</code>
   */
  public boolean hasNamespace() {
    return namespace_ != null;
  }
  /**
   * <code>.proto.com.linkedin.spiral.Namespace namespace = 1;</code>
   */
  public proto.com.linkedin.spiral.Namespace getNamespace() {
    return namespace_ == null ? proto.com.linkedin.spiral.Namespace.getDefaultInstance() : namespace_;
  }
  /**
   * <code>.proto.com.linkedin.spiral.Namespace namespace = 1;</code>
   */
  public proto.com.linkedin.spiral.NamespaceOrBuilder getNamespaceOrBuilder() {
    return getNamespace();
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
    if (namespace_ != null) {
      output.writeMessage(1, getNamespace());
    }
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (namespace_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, getNamespace());
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
    if (!(obj instanceof proto.com.linkedin.spiral.GetNamespaceResponse)) {
      return super.equals(obj);
    }
    proto.com.linkedin.spiral.GetNamespaceResponse other = (proto.com.linkedin.spiral.GetNamespaceResponse) obj;

    boolean result = true;
    result = result && (hasNamespace() == other.hasNamespace());
    if (hasNamespace()) {
      result = result && getNamespace()
          .equals(other.getNamespace());
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
    if (hasNamespace()) {
      hash = (37 * hash) + NAMESPACE_FIELD_NUMBER;
      hash = (53 * hash) + getNamespace().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static proto.com.linkedin.spiral.GetNamespaceResponse parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static proto.com.linkedin.spiral.GetNamespaceResponse parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static proto.com.linkedin.spiral.GetNamespaceResponse parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static proto.com.linkedin.spiral.GetNamespaceResponse parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static proto.com.linkedin.spiral.GetNamespaceResponse parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static proto.com.linkedin.spiral.GetNamespaceResponse parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static proto.com.linkedin.spiral.GetNamespaceResponse parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static proto.com.linkedin.spiral.GetNamespaceResponse parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static proto.com.linkedin.spiral.GetNamespaceResponse parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static proto.com.linkedin.spiral.GetNamespaceResponse parseFrom(
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
  public static Builder newBuilder(proto.com.linkedin.spiral.GetNamespaceResponse prototype) {
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
   * Protobuf type {@code proto.com.linkedin.spiral.GetNamespaceResponse}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:proto.com.linkedin.spiral.GetNamespaceResponse)
      proto.com.linkedin.spiral.GetNamespaceResponseOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return proto.com.linkedin.spiral.SpiralApiOuterClass.internal_static_proto_com_linkedin_spiral_GetNamespaceResponse_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return proto.com.linkedin.spiral.SpiralApiOuterClass.internal_static_proto_com_linkedin_spiral_GetNamespaceResponse_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              proto.com.linkedin.spiral.GetNamespaceResponse.class, proto.com.linkedin.spiral.GetNamespaceResponse.Builder.class);
    }

    // Construct using proto.com.linkedin.spiral.GetNamespaceResponse.newBuilder()
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
      if (namespaceBuilder_ == null) {
        namespace_ = null;
      } else {
        namespace_ = null;
        namespaceBuilder_ = null;
      }
      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return proto.com.linkedin.spiral.SpiralApiOuterClass.internal_static_proto_com_linkedin_spiral_GetNamespaceResponse_descriptor;
    }

    public proto.com.linkedin.spiral.GetNamespaceResponse getDefaultInstanceForType() {
      return proto.com.linkedin.spiral.GetNamespaceResponse.getDefaultInstance();
    }

    public proto.com.linkedin.spiral.GetNamespaceResponse build() {
      proto.com.linkedin.spiral.GetNamespaceResponse result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public proto.com.linkedin.spiral.GetNamespaceResponse buildPartial() {
      proto.com.linkedin.spiral.GetNamespaceResponse result = new proto.com.linkedin.spiral.GetNamespaceResponse(this);
      if (namespaceBuilder_ == null) {
        result.namespace_ = namespace_;
      } else {
        result.namespace_ = namespaceBuilder_.build();
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
      if (other instanceof proto.com.linkedin.spiral.GetNamespaceResponse) {
        return mergeFrom((proto.com.linkedin.spiral.GetNamespaceResponse)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(proto.com.linkedin.spiral.GetNamespaceResponse other) {
      if (other == proto.com.linkedin.spiral.GetNamespaceResponse.getDefaultInstance()) return this;
      if (other.hasNamespace()) {
        mergeNamespace(other.getNamespace());
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
      proto.com.linkedin.spiral.GetNamespaceResponse parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (proto.com.linkedin.spiral.GetNamespaceResponse) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private proto.com.linkedin.spiral.Namespace namespace_ = null;
    private com.google.protobuf.SingleFieldBuilderV3<
        proto.com.linkedin.spiral.Namespace, proto.com.linkedin.spiral.Namespace.Builder, proto.com.linkedin.spiral.NamespaceOrBuilder> namespaceBuilder_;
    /**
     * <code>.proto.com.linkedin.spiral.Namespace namespace = 1;</code>
     */
    public boolean hasNamespace() {
      return namespaceBuilder_ != null || namespace_ != null;
    }
    /**
     * <code>.proto.com.linkedin.spiral.Namespace namespace = 1;</code>
     */
    public proto.com.linkedin.spiral.Namespace getNamespace() {
      if (namespaceBuilder_ == null) {
        return namespace_ == null ? proto.com.linkedin.spiral.Namespace.getDefaultInstance() : namespace_;
      } else {
        return namespaceBuilder_.getMessage();
      }
    }
    /**
     * <code>.proto.com.linkedin.spiral.Namespace namespace = 1;</code>
     */
    public Builder setNamespace(proto.com.linkedin.spiral.Namespace value) {
      if (namespaceBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        namespace_ = value;
        onChanged();
      } else {
        namespaceBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <code>.proto.com.linkedin.spiral.Namespace namespace = 1;</code>
     */
    public Builder setNamespace(
        proto.com.linkedin.spiral.Namespace.Builder builderForValue) {
      if (namespaceBuilder_ == null) {
        namespace_ = builderForValue.build();
        onChanged();
      } else {
        namespaceBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <code>.proto.com.linkedin.spiral.Namespace namespace = 1;</code>
     */
    public Builder mergeNamespace(proto.com.linkedin.spiral.Namespace value) {
      if (namespaceBuilder_ == null) {
        if (namespace_ != null) {
          namespace_ =
            proto.com.linkedin.spiral.Namespace.newBuilder(namespace_).mergeFrom(value).buildPartial();
        } else {
          namespace_ = value;
        }
        onChanged();
      } else {
        namespaceBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <code>.proto.com.linkedin.spiral.Namespace namespace = 1;</code>
     */
    public Builder clearNamespace() {
      if (namespaceBuilder_ == null) {
        namespace_ = null;
        onChanged();
      } else {
        namespace_ = null;
        namespaceBuilder_ = null;
      }

      return this;
    }
    /**
     * <code>.proto.com.linkedin.spiral.Namespace namespace = 1;</code>
     */
    public proto.com.linkedin.spiral.Namespace.Builder getNamespaceBuilder() {
      
      onChanged();
      return getNamespaceFieldBuilder().getBuilder();
    }
    /**
     * <code>.proto.com.linkedin.spiral.Namespace namespace = 1;</code>
     */
    public proto.com.linkedin.spiral.NamespaceOrBuilder getNamespaceOrBuilder() {
      if (namespaceBuilder_ != null) {
        return namespaceBuilder_.getMessageOrBuilder();
      } else {
        return namespace_ == null ?
            proto.com.linkedin.spiral.Namespace.getDefaultInstance() : namespace_;
      }
    }
    /**
     * <code>.proto.com.linkedin.spiral.Namespace namespace = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        proto.com.linkedin.spiral.Namespace, proto.com.linkedin.spiral.Namespace.Builder, proto.com.linkedin.spiral.NamespaceOrBuilder> 
        getNamespaceFieldBuilder() {
      if (namespaceBuilder_ == null) {
        namespaceBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            proto.com.linkedin.spiral.Namespace, proto.com.linkedin.spiral.Namespace.Builder, proto.com.linkedin.spiral.NamespaceOrBuilder>(
                getNamespace(),
                getParentForChildren(),
                isClean());
        namespace_ = null;
      }
      return namespaceBuilder_;
    }
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return this;
    }

    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return this;
    }


    // @@protoc_insertion_point(builder_scope:proto.com.linkedin.spiral.GetNamespaceResponse)
  }

  // @@protoc_insertion_point(class_scope:proto.com.linkedin.spiral.GetNamespaceResponse)
  private static final proto.com.linkedin.spiral.GetNamespaceResponse DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new proto.com.linkedin.spiral.GetNamespaceResponse();
  }

  public static proto.com.linkedin.spiral.GetNamespaceResponse getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<GetNamespaceResponse>
      PARSER = new com.google.protobuf.AbstractParser<GetNamespaceResponse>() {
    public GetNamespaceResponse parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return new GetNamespaceResponse(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<GetNamespaceResponse> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<GetNamespaceResponse> getParserForType() {
    return PARSER;
  }

  public proto.com.linkedin.spiral.GetNamespaceResponse getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

