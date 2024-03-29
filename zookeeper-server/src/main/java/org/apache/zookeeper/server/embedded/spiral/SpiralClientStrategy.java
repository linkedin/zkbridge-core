package org.apache.zookeeper.server.embedded.spiral;

import org.apache.zookeeper.server.ZKBServerConfig;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig.ConfigException;
import org.apache.zookeeper.spiral.SpiralClient;
import org.apache.zookeeper.spiral.SpiralClientImpl;


public interface SpiralClientStrategy {

  public static class Builder {

    /**
     * Default builders
     */
    public static PassThroughSpiralClientStrategy passThrough() {
      return new PassThroughSpiralClientStrategy();
    }

    public static InMemorySpiralClientStrategy inMem() {
      return new InMemorySpiralClientStrategy();
    }

    public static SpiralClientFromConfigBasedStrategy configBased() {
      return new SpiralClientFromConfigBasedStrategy();
    }

    public static ExternalSpiralClientStrategy external() {
      return new ExternalSpiralClientStrategy();
    }

  }

  /**
   * Strategy Definitions
   */
  class PassThroughSpiralClientStrategy implements SpiralClientStrategy {
    private SpiralClient spiralClient;

    public PassThroughSpiralClientStrategy setSpiralClient(SpiralClient spiralClient) {
      this.spiralClient = spiralClient;
      return this;
    }
  }

  class InMemorySpiralClientStrategy implements SpiralClientStrategy {
    private InMemoryFS inMemoryFS = new InMemoryFS();

    public InMemorySpiralClientStrategy inMemoryFS(InMemoryFS inMemoryFS) {
      this.inMemoryFS = inMemoryFS;
      return this;
    }
  }

  class SpiralClientFromConfigBasedStrategy implements SpiralClientStrategy {
    private String configFileLoc;

    public SpiralClientFromConfigBasedStrategy configFile(String configFileLoc) {
      this.configFileLoc = configFileLoc;
      return this;
    }
  }

  class ExternalSpiralClientStrategy implements SpiralClientStrategy {
    private String identityCert;
    private String identityKey;
    private String spiralEndpoint;
    private String overrideAuthority;
    private String spiralNamespace;
    private String caBundle = "/etc/riddler/ca-bundle.crt";

    public ExternalSpiralClientStrategy identityCert(String identityCert) {
      this.identityCert = identityCert;
      return this;
    }

    public ExternalSpiralClientStrategy identityKey(String identityKey) {
      this.identityKey = identityKey;
      return this;
    }

    public ExternalSpiralClientStrategy spiralEndpoint(String spiralEndpoint) {
      this.spiralEndpoint = spiralEndpoint;
      return this;
    }

    public ExternalSpiralClientStrategy overrideAuthority(String overrideAuthority) {
      this.overrideAuthority = overrideAuthority;
      return this;
    }

    public ExternalSpiralClientStrategy spiralNamespace(String spiralNamespace) {
      this.spiralNamespace = spiralNamespace;
      return this;
    }
  }

  /**
   * Builder function that generates the Spiral client
   */
  default SpiralClient buildSpiralClient() throws ConfigException {
    if (this instanceof PassThroughSpiralClientStrategy) {
      PassThroughSpiralClientStrategy strategy = (PassThroughSpiralClientStrategy) this;
      return strategy.spiralClient;
    }
    if (this instanceof InMemorySpiralClientStrategy) {
      InMemorySpiralClientStrategy strategy = (InMemorySpiralClientStrategy) this;
      return new InMemorySpiralClient(strategy.inMemoryFS);
    }
    if (this instanceof ExternalSpiralClientStrategy) {
      ExternalSpiralClientStrategy strategy = (ExternalSpiralClientStrategy) this;
      return new SpiralClientImpl.SpiralClientBuilder()
          .setSpiralEndpoint(strategy.spiralEndpoint)
          .setIdentityCert(strategy.identityCert)
          .setIdentityKey(strategy.identityKey)
          .setCaBundle(strategy.caBundle)
          .setOverrideAuthority(strategy.overrideAuthority)
          .setNamespace(strategy.spiralNamespace)
          .build();
    }
    if (this instanceof SpiralClientFromConfigBasedStrategy) {
      SpiralClientFromConfigBasedStrategy strategy = (SpiralClientFromConfigBasedStrategy) this;
      ZKBServerConfig config = new ZKBServerConfig();
      config.parse(strategy.configFileLoc);

      if (!config.isSpiralEnabled()) {
        throw new IllegalStateException("cannot build spiral client when the spiral is disabled.");
      }

      return new SpiralClientImpl.SpiralClientBuilder()
          .setSpiralEndpoint(config.getSpiralEndpoint())
          .setIdentityCert(config.getIdentityCert())
          .setIdentityKey(config.getIdentityKey())
          .setCaBundle(config.getCaBundle())
          .setOverrideAuthority(config.getOverrideAuthority())
          .setNamespace(config.getSpiralNamespace())
          .build();
    }

    throw new IllegalStateException("error while building spiral client!");
  }

}
