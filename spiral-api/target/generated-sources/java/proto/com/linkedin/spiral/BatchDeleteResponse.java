// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: SpiralApi.proto

package proto.com.linkedin.spiral;

/**
 * Protobuf type {@code proto.com.linkedin.spiral.BatchDeleteResponse}
 */
public  final class BatchDeleteResponse extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:proto.com.linkedin.spiral.BatchDeleteResponse)
    BatchDeleteResponseOrBuilder {
  // Use BatchDeleteResponse.newBuilder() to construct.
  private BatchDeleteResponse(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private BatchDeleteResponse() {
    errors_ = java.util.Collections.emptyList();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
  }
  private BatchDeleteResponse(
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
              errors_ = new java.util.ArrayList<proto.com.linkedin.spiral.Error>();
              mutable_bitField0_ |= 0x00000001;
            }
            errors_.add(
                input.readMessage(proto.com.linkedin.spiral.Error.parser(), extensionRegistry));
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
        errors_ = java.util.Collections.unmodifiableList(errors_);
      }
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return proto.com.linkedin.spiral.SpiralApiOuterClass.internal_static_proto_com_linkedin_spiral_BatchDeleteResponse_descriptor;
  }

  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return proto.com.linkedin.spiral.SpiralApiOuterClass.internal_static_proto_com_linkedin_spiral_BatchDeleteResponse_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            proto.com.linkedin.spiral.BatchDeleteResponse.class, proto.com.linkedin.spiral.BatchDeleteResponse.Builder.class);
  }

  public static final int ERRORS_FIELD_NUMBER = 1;
  private java.util.List<proto.com.linkedin.spiral.Error> errors_;
  /**
   * <code>repeated .proto.com.linkedin.spiral.Error errors = 1;</code>
   */
  public java.util.List<proto.com.linkedin.spiral.Error> getErrorsList() {
    return errors_;
  }
  /**
   * <code>repeated .proto.com.linkedin.spiral.Error errors = 1;</code>
   */
  public java.util.List<? extends proto.com.linkedin.spiral.ErrorOrBuilder> 
      getErrorsOrBuilderList() {
    return errors_;
  }
  /**
   * <code>repeated .proto.com.linkedin.spiral.Error errors = 1;</code>
   */
  public int getErrorsCount() {
    return errors_.size();
  }
  /**
   * <code>repeated .proto.com.linkedin.spiral.Error errors = 1;</code>
   */
  public proto.com.linkedin.spiral.Error getErrors(int index) {
    return errors_.get(index);
  }
  /**
   * <code>repeated .proto.com.linkedin.spiral.Error errors = 1;</code>
   */
  public proto.com.linkedin.spiral.ErrorOrBuilder getErrorsOrBuilder(
      int index) {
    return errors_.get(index);
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
    for (int i = 0; i < errors_.size(); i++) {
      output.writeMessage(1, errors_.get(i));
    }
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    for (int i = 0; i < errors_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, errors_.get(i));
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
    if (!(obj instanceof proto.com.linkedin.spiral.BatchDeleteResponse)) {
      return super.equals(obj);
    }
    proto.com.linkedin.spiral.BatchDeleteResponse other = (proto.com.linkedin.spiral.BatchDeleteResponse) obj;

    boolean result = true;
    result = result && getErrorsList()
        .equals(other.getErrorsList());
    return result;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    if (getErrorsCount() > 0) {
      hash = (37 * hash) + ERRORS_FIELD_NUMBER;
      hash = (53 * hash) + getErrorsList().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static proto.com.linkedin.spiral.BatchDeleteResponse parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static proto.com.linkedin.spiral.BatchDeleteResponse parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static proto.com.linkedin.spiral.BatchDeleteResponse parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static proto.com.linkedin.spiral.BatchDeleteResponse parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static proto.com.linkedin.spiral.BatchDeleteResponse parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static proto.com.linkedin.spiral.BatchDeleteResponse parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static proto.com.linkedin.spiral.BatchDeleteResponse parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static proto.com.linkedin.spiral.BatchDeleteResponse parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static proto.com.linkedin.spiral.BatchDeleteResponse parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static proto.com.linkedin.spiral.BatchDeleteResponse parseFrom(
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
  public static Builder newBuilder(proto.com.linkedin.spiral.BatchDeleteResponse prototype) {
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
   * Protobuf type {@code proto.com.linkedin.spiral.BatchDeleteResponse}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:proto.com.linkedin.spiral.BatchDeleteResponse)
      proto.com.linkedin.spiral.BatchDeleteResponseOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return proto.com.linkedin.spiral.SpiralApiOuterClass.internal_static_proto_com_linkedin_spiral_BatchDeleteResponse_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return proto.com.linkedin.spiral.SpiralApiOuterClass.internal_static_proto_com_linkedin_spiral_BatchDeleteResponse_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              proto.com.linkedin.spiral.BatchDeleteResponse.class, proto.com.linkedin.spiral.BatchDeleteResponse.Builder.class);
    }

    // Construct using proto.com.linkedin.spiral.BatchDeleteResponse.newBuilder()
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
        getErrorsFieldBuilder();
      }
    }
    public Builder clear() {
      super.clear();
      if (errorsBuilder_ == null) {
        errors_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
      } else {
        errorsBuilder_.clear();
      }
      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return proto.com.linkedin.spiral.SpiralApiOuterClass.internal_static_proto_com_linkedin_spiral_BatchDeleteResponse_descriptor;
    }

    public proto.com.linkedin.spiral.BatchDeleteResponse getDefaultInstanceForType() {
      return proto.com.linkedin.spiral.BatchDeleteResponse.getDefaultInstance();
    }

    public proto.com.linkedin.spiral.BatchDeleteResponse build() {
      proto.com.linkedin.spiral.BatchDeleteResponse result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public proto.com.linkedin.spiral.BatchDeleteResponse buildPartial() {
      proto.com.linkedin.spiral.BatchDeleteResponse result = new proto.com.linkedin.spiral.BatchDeleteResponse(this);
      int from_bitField0_ = bitField0_;
      if (errorsBuilder_ == null) {
        if (((bitField0_ & 0x00000001) == 0x00000001)) {
          errors_ = java.util.Collections.unmodifiableList(errors_);
          bitField0_ = (bitField0_ & ~0x00000001);
        }
        result.errors_ = errors_;
      } else {
        result.errors_ = errorsBuilder_.build();
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
      if (other instanceof proto.com.linkedin.spiral.BatchDeleteResponse) {
        return mergeFrom((proto.com.linkedin.spiral.BatchDeleteResponse)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(proto.com.linkedin.spiral.BatchDeleteResponse other) {
      if (other == proto.com.linkedin.spiral.BatchDeleteResponse.getDefaultInstance()) return this;
      if (errorsBuilder_ == null) {
        if (!other.errors_.isEmpty()) {
          if (errors_.isEmpty()) {
            errors_ = other.errors_;
            bitField0_ = (bitField0_ & ~0x00000001);
          } else {
            ensureErrorsIsMutable();
            errors_.addAll(other.errors_);
          }
          onChanged();
        }
      } else {
        if (!other.errors_.isEmpty()) {
          if (errorsBuilder_.isEmpty()) {
            errorsBuilder_.dispose();
            errorsBuilder_ = null;
            errors_ = other.errors_;
            bitField0_ = (bitField0_ & ~0x00000001);
            errorsBuilder_ = 
              com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                 getErrorsFieldBuilder() : null;
          } else {
            errorsBuilder_.addAllMessages(other.errors_);
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
      proto.com.linkedin.spiral.BatchDeleteResponse parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (proto.com.linkedin.spiral.BatchDeleteResponse) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private java.util.List<proto.com.linkedin.spiral.Error> errors_ =
      java.util.Collections.emptyList();
    private void ensureErrorsIsMutable() {
      if (!((bitField0_ & 0x00000001) == 0x00000001)) {
        errors_ = new java.util.ArrayList<proto.com.linkedin.spiral.Error>(errors_);
        bitField0_ |= 0x00000001;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
        proto.com.linkedin.spiral.Error, proto.com.linkedin.spiral.Error.Builder, proto.com.linkedin.spiral.ErrorOrBuilder> errorsBuilder_;

    /**
     * <code>repeated .proto.com.linkedin.spiral.Error errors = 1;</code>
     */
    public java.util.List<proto.com.linkedin.spiral.Error> getErrorsList() {
      if (errorsBuilder_ == null) {
        return java.util.Collections.unmodifiableList(errors_);
      } else {
        return errorsBuilder_.getMessageList();
      }
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Error errors = 1;</code>
     */
    public int getErrorsCount() {
      if (errorsBuilder_ == null) {
        return errors_.size();
      } else {
        return errorsBuilder_.getCount();
      }
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Error errors = 1;</code>
     */
    public proto.com.linkedin.spiral.Error getErrors(int index) {
      if (errorsBuilder_ == null) {
        return errors_.get(index);
      } else {
        return errorsBuilder_.getMessage(index);
      }
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Error errors = 1;</code>
     */
    public Builder setErrors(
        int index, proto.com.linkedin.spiral.Error value) {
      if (errorsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureErrorsIsMutable();
        errors_.set(index, value);
        onChanged();
      } else {
        errorsBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Error errors = 1;</code>
     */
    public Builder setErrors(
        int index, proto.com.linkedin.spiral.Error.Builder builderForValue) {
      if (errorsBuilder_ == null) {
        ensureErrorsIsMutable();
        errors_.set(index, builderForValue.build());
        onChanged();
      } else {
        errorsBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Error errors = 1;</code>
     */
    public Builder addErrors(proto.com.linkedin.spiral.Error value) {
      if (errorsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureErrorsIsMutable();
        errors_.add(value);
        onChanged();
      } else {
        errorsBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Error errors = 1;</code>
     */
    public Builder addErrors(
        int index, proto.com.linkedin.spiral.Error value) {
      if (errorsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureErrorsIsMutable();
        errors_.add(index, value);
        onChanged();
      } else {
        errorsBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Error errors = 1;</code>
     */
    public Builder addErrors(
        proto.com.linkedin.spiral.Error.Builder builderForValue) {
      if (errorsBuilder_ == null) {
        ensureErrorsIsMutable();
        errors_.add(builderForValue.build());
        onChanged();
      } else {
        errorsBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Error errors = 1;</code>
     */
    public Builder addErrors(
        int index, proto.com.linkedin.spiral.Error.Builder builderForValue) {
      if (errorsBuilder_ == null) {
        ensureErrorsIsMutable();
        errors_.add(index, builderForValue.build());
        onChanged();
      } else {
        errorsBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Error errors = 1;</code>
     */
    public Builder addAllErrors(
        java.lang.Iterable<? extends proto.com.linkedin.spiral.Error> values) {
      if (errorsBuilder_ == null) {
        ensureErrorsIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, errors_);
        onChanged();
      } else {
        errorsBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Error errors = 1;</code>
     */
    public Builder clearErrors() {
      if (errorsBuilder_ == null) {
        errors_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
      } else {
        errorsBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Error errors = 1;</code>
     */
    public Builder removeErrors(int index) {
      if (errorsBuilder_ == null) {
        ensureErrorsIsMutable();
        errors_.remove(index);
        onChanged();
      } else {
        errorsBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Error errors = 1;</code>
     */
    public proto.com.linkedin.spiral.Error.Builder getErrorsBuilder(
        int index) {
      return getErrorsFieldBuilder().getBuilder(index);
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Error errors = 1;</code>
     */
    public proto.com.linkedin.spiral.ErrorOrBuilder getErrorsOrBuilder(
        int index) {
      if (errorsBuilder_ == null) {
        return errors_.get(index);  } else {
        return errorsBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Error errors = 1;</code>
     */
    public java.util.List<? extends proto.com.linkedin.spiral.ErrorOrBuilder> 
         getErrorsOrBuilderList() {
      if (errorsBuilder_ != null) {
        return errorsBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(errors_);
      }
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Error errors = 1;</code>
     */
    public proto.com.linkedin.spiral.Error.Builder addErrorsBuilder() {
      return getErrorsFieldBuilder().addBuilder(
          proto.com.linkedin.spiral.Error.getDefaultInstance());
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Error errors = 1;</code>
     */
    public proto.com.linkedin.spiral.Error.Builder addErrorsBuilder(
        int index) {
      return getErrorsFieldBuilder().addBuilder(
          index, proto.com.linkedin.spiral.Error.getDefaultInstance());
    }
    /**
     * <code>repeated .proto.com.linkedin.spiral.Error errors = 1;</code>
     */
    public java.util.List<proto.com.linkedin.spiral.Error.Builder> 
         getErrorsBuilderList() {
      return getErrorsFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilderV3<
        proto.com.linkedin.spiral.Error, proto.com.linkedin.spiral.Error.Builder, proto.com.linkedin.spiral.ErrorOrBuilder> 
        getErrorsFieldBuilder() {
      if (errorsBuilder_ == null) {
        errorsBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
            proto.com.linkedin.spiral.Error, proto.com.linkedin.spiral.Error.Builder, proto.com.linkedin.spiral.ErrorOrBuilder>(
                errors_,
                ((bitField0_ & 0x00000001) == 0x00000001),
                getParentForChildren(),
                isClean());
        errors_ = null;
      }
      return errorsBuilder_;
    }
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return this;
    }

    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return this;
    }


    // @@protoc_insertion_point(builder_scope:proto.com.linkedin.spiral.BatchDeleteResponse)
  }

  // @@protoc_insertion_point(class_scope:proto.com.linkedin.spiral.BatchDeleteResponse)
  private static final proto.com.linkedin.spiral.BatchDeleteResponse DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new proto.com.linkedin.spiral.BatchDeleteResponse();
  }

  public static proto.com.linkedin.spiral.BatchDeleteResponse getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<BatchDeleteResponse>
      PARSER = new com.google.protobuf.AbstractParser<BatchDeleteResponse>() {
    public BatchDeleteResponse parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return new BatchDeleteResponse(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<BatchDeleteResponse> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<BatchDeleteResponse> getParserForType() {
    return PARSER;
  }

  public proto.com.linkedin.spiral.BatchDeleteResponse getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

