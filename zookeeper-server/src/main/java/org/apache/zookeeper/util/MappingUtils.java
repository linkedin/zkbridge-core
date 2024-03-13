package org.apache.zookeeper.util;

import org.apache.zookeeper.txn.ServerAwareTxnHeader;
import org.apache.zookeeper.txn.TxnHeader;


/**
 * Class responsible for performing mapping across various data models.
 */
public class MappingUtils {

  private MappingUtils() {
  }

  public static TxnHeader toTxnHeader(ServerAwareTxnHeader saHdr) {
    return new TxnHeader(saHdr.getClientId(), saHdr.getCxid(), saHdr.getZxid(), saHdr.getTime(), saHdr.getType());
  }
}
