// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: SpiralApi.proto

package proto.com.linkedin.spiral;

public interface BatchDeleteRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:proto.com.linkedin.spiral.BatchDeleteRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.proto.com.linkedin.spiral.SpiralContext spiralContext = 1;</code>
   */
  boolean hasSpiralContext();
  /**
   * <code>.proto.com.linkedin.spiral.SpiralContext spiralContext = 1;</code>
   */
  proto.com.linkedin.spiral.SpiralContext getSpiralContext();
  /**
   * <code>.proto.com.linkedin.spiral.SpiralContext spiralContext = 1;</code>
   */
  proto.com.linkedin.spiral.SpiralContextOrBuilder getSpiralContextOrBuilder();

  /**
   * <code>repeated .proto.com.linkedin.spiral.Key keys = 2;</code>
   */
  java.util.List<proto.com.linkedin.spiral.Key> 
      getKeysList();
  /**
   * <code>repeated .proto.com.linkedin.spiral.Key keys = 2;</code>
   */
  proto.com.linkedin.spiral.Key getKeys(int index);
  /**
   * <code>repeated .proto.com.linkedin.spiral.Key keys = 2;</code>
   */
  int getKeysCount();
  /**
   * <code>repeated .proto.com.linkedin.spiral.Key keys = 2;</code>
   */
  java.util.List<? extends proto.com.linkedin.spiral.KeyOrBuilder> 
      getKeysOrBuilderList();
  /**
   * <code>repeated .proto.com.linkedin.spiral.Key keys = 2;</code>
   */
  proto.com.linkedin.spiral.KeyOrBuilder getKeysOrBuilder(
      int index);
}
