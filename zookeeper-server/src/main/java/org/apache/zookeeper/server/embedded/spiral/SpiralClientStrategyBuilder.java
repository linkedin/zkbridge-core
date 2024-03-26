package org.apache.zookeeper.server.embedded.spiral;

import org.apache.zookeeper.server.embedded.spiral.SpiralClientStrategy.*;


public interface SpiralClientStrategyBuilder {

  /**
   * Default builders
   */
  static PassThroughSpiralClientStrategy passThrough() {
    return new PassThroughSpiralClientStrategy();
  }

  static InMemorySpiralClientStrategy inMem() {
    return new InMemorySpiralClientStrategy();
  }

  static SpiralClientFromConfigBasedStrategy configBased() {
    return new SpiralClientFromConfigBasedStrategy();
  }

  static ExternalSpiralClientStrategy external() {
    return new ExternalSpiralClientStrategy();
  }

}
