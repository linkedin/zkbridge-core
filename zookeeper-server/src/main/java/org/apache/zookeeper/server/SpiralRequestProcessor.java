/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.zookeeper.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import org.apache.jute.BinaryOutputArchive;
import org.apache.jute.Record;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.BadArgumentsException;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.MultiOperationRecord;
import org.apache.zookeeper.Op;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooDefs.OpCode;
import org.apache.zookeeper.common.PathUtils;
import org.apache.zookeeper.common.StringUtils;
import org.apache.zookeeper.common.Time;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.StatPersisted;
import org.apache.zookeeper.proto.CheckVersionRequest;
import org.apache.zookeeper.proto.CreateRequest;
import org.apache.zookeeper.proto.CreateTTLRequest;
import org.apache.zookeeper.proto.DeleteRequest;
import org.apache.zookeeper.proto.ReconfigRequest;
import org.apache.zookeeper.proto.SetACLRequest;
import org.apache.zookeeper.proto.SetDataRequest;
import org.apache.zookeeper.server.ZooKeeperServer.ChangeRecord;
import org.apache.zookeeper.server.ZooKeeperServer.PrecalculatedDigest;
import org.apache.zookeeper.server.auth.ProviderRegistry;
import org.apache.zookeeper.server.auth.ServerAuthenticationProvider;
import org.apache.zookeeper.server.auth.X509AuthenticationConfig;
import org.apache.zookeeper.server.auth.X509AuthenticationUtil;
import org.apache.zookeeper.txn.CheckVersionTxn;
import org.apache.zookeeper.txn.CloseSessionTxn;
import org.apache.zookeeper.txn.CreateContainerTxn;
import org.apache.zookeeper.txn.CreateSessionTxn;
import org.apache.zookeeper.txn.CreateTTLTxn;
import org.apache.zookeeper.txn.CreateTxn;
import org.apache.zookeeper.txn.DeleteTxn;
import org.apache.zookeeper.txn.ErrorTxn;
import org.apache.zookeeper.txn.MultiTxn;
import org.apache.zookeeper.txn.SetACLTxn;
import org.apache.zookeeper.txn.SetDataTxn;
import org.apache.zookeeper.txn.Txn;
import org.apache.zookeeper.txn.TxnDigest;
import org.apache.zookeeper.txn.TxnHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This request processor is generally at the start of a RequestProcessor
 * change. It sets up any transactions associated with requests that change the
 * state of the system. It counts on ZooKeeperServer to update
 * outstandingRequests, so that it can take into account transactions that are
 * in the queue to be applied when generating a transaction.
 */
public class SpiralRequestProcessor extends ZooKeeperCriticalThread implements RequestProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(SpiralRequestProcessor.class);

    /**
     * this is only for testing purposes.
     * should never be used otherwise
     * TODO: Only one test case is using, not sure,
     */
    private static boolean failCreate = false;

    LinkedBlockingQueue<Request> submittedRequests = new LinkedBlockingQueue<Request>();

    private final RequestProcessor nextProcessor;

    //Zkbridge will have digestEnabled = false
    private final boolean digestEnabled;
    private DigestCalculator digestCalculator;

    ZooKeeperServer zks;

    public enum DigestOpCode {
        NOOP, ADD, REMOVE, UPDATE;
    }

    public SpiralRequestProcessor(ZooKeeperServer zks, RequestProcessor nextProcessor) {
        super(
            "ProcessThread(sid:" + zks.getServerId()
            + " cport:" + zks.getClientPort()
            + "):", zks.getZooKeeperServerListener());
        this.nextProcessor = nextProcessor;
        this.zks = zks;
        this.digestEnabled = ZooKeeperServer.isDigestEnabled();
        if (this.digestEnabled) {
            this.digestCalculator = new DigestCalculator();
        }
    }

    /**
     * method for tests to set failCreate
     * @param b
     */
    public static void setFailCreate(boolean b) {
        failCreate = b;
    }


    @Override
    public void run() {
        LOG.info(String.format("SpiralRequestProcessor (sid:%d) started, reconfigEnabled=%s", zks.getServerId(), zks.reconfigEnabled));
        try {
            while (true) {
                ServerMetrics.getMetrics().PREP_PROCESSOR_QUEUE_SIZE.add(submittedRequests.size());
                Request request = submittedRequests.take();
                ServerMetrics.getMetrics().PREP_PROCESSOR_QUEUE_TIME
                    .add(Time.currentElapsedTime() - request.prepQueueStartTime);
                long traceMask = ZooTrace.CLIENT_REQUEST_TRACE_MASK;
                if (request.type == OpCode.ping) {
                    traceMask = ZooTrace.CLIENT_PING_TRACE_MASK;
                }
                if (LOG.isTraceEnabled()) {
                    ZooTrace.logRequest(LOG, traceMask, 'P', request, "");
                }
                if (Request.requestOfDeath == request) {
                    break;
                }

                request.prepStartTime = Time.currentElapsedTime();
                pRequest(request);
            }
        } catch (Exception e) {
            handleException(this.getName(), e);
        }
        LOG.info("SpiralRequestProcessor exited loop!");
    }

    private ChangeRecord getRecordForPath(String path) throws KeeperException.NoNodeException {
    if (zks.isSpiralEnabled()) {
       return getSpiralRecordForPath(path);
    }
    ChangeRecord lastChange = null;
    synchronized (zks.outstandingChanges) {
        lastChange = zks.outstandingChangesForPath.get(path);
        if (lastChange == null) {
            DataNode n = zks.getZKDatabase().getNode(path);
            if (n != null) {
                Set<String> children;
                synchronized (n) {
                    children = n.getChildren();
                }
                lastChange = new ChangeRecord(-1, path, n.stat, children.size(), zks.getZKDatabase().aclForNode(n));

                if (digestEnabled) {
                    lastChange.precalculatedDigest = new PrecalculatedDigest(
                        digestCalculator.calculateDigest(path, n), 0);
                }
                lastChange.data = n.getData();
            }
        }
    }
        if (lastChange == null || lastChange.stat == null) {
        throw new KeeperException.NoNodeException(path);
    }
        return lastChange;
}

    // Use Spiral to get the Record for the path.

    private ChangeRecord getSpiralRecordForPath(String path) throws KeeperException.NoNodeException {
        if (path == null || path.isEmpty()) {
            // create base /zookeeper node
            StatPersisted s = DataTree.createStat(0L, Time.currentWallTime(), 0);
            String emptyString= "";
            byte[] emptyBytes = emptyString.getBytes();
            SpiralNode spiral = new SpiralNode(emptyBytes, 0L, s);
            try {
                zks.createSpiralRecord("/", spiral);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new ChangeRecord(-1, path, s, 0, new ArrayList<>());
        }
        SpiralNode node = zks.getSpiralRecord(path);
        if (node == null) {
            throw new KeeperException.NoNodeException(path);
        }
        ChangeRecord lastChange = new ChangeRecord(-1, path, node.stat, node.childrenCount, new ArrayList<>() /*node.acl*/);
        lastChange.data = node.data;
        lastChange.childCount = node.childrenCount;
        lastChange.ttl = node.ttl;
        lastChange.chroot = node.chroot;
        lastChange.lastSeq = node.lastSeq;

        if (lastChange == null || lastChange.stat == null) {
            throw new KeeperException.NoNodeException(path);
        }
        return lastChange;
    }

    private ChangeRecord getOutstandingChange(String path) {
        synchronized (zks.outstandingChanges) {
            return zks.outstandingChangesForPath.get(path);
        }
    }

    protected void addChangeRecord(ChangeRecord c) {
        synchronized (zks.outstandingChanges) {
            zks.outstandingChanges.add(c);
            zks.outstandingChangesForPath.put(c.path, c);
            ServerMetrics.getMetrics().OUTSTANDING_CHANGES_QUEUED.add(1);
        }
    }


    /**
     * Performs basic validation of a path for a create request.
     * Throws if the path is not valid and returns the parent path.
     * @throws BadArgumentsException
     */
    private String validatePathForCreate(String path, long sessionId) throws BadArgumentsException {
        int lastSlash = path.lastIndexOf('/');
        if (lastSlash == -1 || path.indexOf('\0') != -1 || failCreate) {
            LOG.info("Invalid path {} with session 0x{}", path, Long.toHexString(sessionId));
            throw new KeeperException.BadArgumentsException(path);
        }
        return path.substring(0, lastSlash);
    }

    /**
     * This method will be called inside the ProcessRequestThread, which is a
     * singleton, so there will be a single thread calling this code.
     *
     * @param type
     * @param zxid
     * @param request
     * @param record
     */
    protected void pRequest2Txn(int type, long zxid, Request request, Record record, boolean deserialize) throws KeeperException, IOException, RequestProcessorException {
        if (request.getHdr() == null) {
            request.setHdr(new TxnHeader(request.sessionId, request.cxid, zxid,
                    Time.currentWallTime(), type));
        }

        switch (type) {
        case OpCode.create:
        case OpCode.create2:
        case OpCode.createTTL:
        case OpCode.createContainer: {
            pRequest2TxnCreate(type, request, record, deserialize);
            break;
        }
        case OpCode.deleteContainer: {
            String path = new String(request.request.array());
            String parentPath = getParentPathAndValidate(path);
            ChangeRecord nodeRecord = getRecordForPath(path);
            if (nodeRecord.childCount > 0) {
                throw new KeeperException.NotEmptyException(path);
            }
            if (EphemeralType.get(nodeRecord.stat.getEphemeralOwner()) == EphemeralType.NORMAL) {
                throw new KeeperException.BadVersionException(path);
            }
            ChangeRecord parentRecord = getRecordForPath(parentPath);
            request.setTxn(new DeleteTxn(path));
            parentRecord = parentRecord.duplicate(request.getHdr().getZxid());
            parentRecord.childCount--;
            parentRecord.stat.setPzxid(request.getHdr().getZxid());
            parentRecord.precalculatedDigest = precalculateDigest(
                    DigestOpCode.UPDATE, parentPath, parentRecord.data, parentRecord.stat);
            addChangeRecord(parentRecord);

            nodeRecord = new ChangeRecord(request.getHdr().getZxid(), path, null, -1, null);
            nodeRecord.precalculatedDigest = precalculateDigest(DigestOpCode.REMOVE, path);
            setTxnDigest(request, nodeRecord.precalculatedDigest);
            addChangeRecord(nodeRecord);
            break;
        }
        case OpCode.delete:
            zks.sessionTracker.checkSession(request.sessionId, request.getOwner());
            DeleteRequest deleteRequest = (DeleteRequest) record;
            if (deserialize) {
                ByteBufferInputStream.byteBuffer2Record(request.request, deleteRequest);
            }
            String path = deleteRequest.getPath();
            String parentPath = getParentPathAndValidate(path);
            ChangeRecord parentRecord = getRecordForPath(parentPath);
            zks.checkACL(request.cnxn, parentRecord.acl, ZooDefs.Perms.DELETE, request.authInfo, path, null);
            ChangeRecord nodeRecord = getRecordForPath(path);
            checkAndIncVersion(nodeRecord.stat.getVersion(), deleteRequest.getVersion(), path);
            if (nodeRecord.childCount > 0) {
                throw new KeeperException.NotEmptyException(path);
            }
            request.setTxn(new DeleteTxn(path));
            parentRecord = parentRecord.duplicate(request.getHdr().getZxid());
            parentRecord.childCount--;
            parentRecord.stat.setPzxid(request.getHdr().getZxid());
            parentRecord.precalculatedDigest = precalculateDigest(
                    DigestOpCode.UPDATE, parentPath, parentRecord.data, parentRecord.stat);
            addChangeRecord(parentRecord);

            nodeRecord = new ChangeRecord(request.getHdr().getZxid(), path, null, -1, null);
            nodeRecord.precalculatedDigest = precalculateDigest(DigestOpCode.REMOVE, path);
            setTxnDigest(request, nodeRecord.precalculatedDigest);
            addChangeRecord(nodeRecord);
            break;
        case OpCode.setData:
            zks.sessionTracker.checkSession(request.sessionId, request.getOwner());
            SetDataRequest setDataRequest = (SetDataRequest) record;
            if (deserialize) {
                ByteBufferInputStream.byteBuffer2Record(request.request, setDataRequest);
            }
            path = setDataRequest.getPath();
            validatePath(path, request.sessionId);
            nodeRecord = getRecordForPath(path);
            zks.checkACL(request.cnxn, nodeRecord.acl, ZooDefs.Perms.WRITE, request.authInfo, path, null);
            int newVersion = checkAndIncVersion(nodeRecord.stat.getVersion(), setDataRequest.getVersion(), path);
            request.setTxn(new SetDataTxn(path, setDataRequest.getData(), newVersion));
            nodeRecord = nodeRecord.duplicate(request.getHdr().getZxid());
            nodeRecord.stat.setVersion(newVersion);
            nodeRecord.stat.setMtime(request.getHdr().getTime());
            nodeRecord.stat.setMzxid(zxid);
            nodeRecord.data = setDataRequest.getData();
            nodeRecord.precalculatedDigest = precalculateDigest(
                    DigestOpCode.UPDATE, path, nodeRecord.data, nodeRecord.stat);
            setTxnDigest(request, nodeRecord.precalculatedDigest);
            addChangeRecord(nodeRecord);
            break;
        case OpCode.reconfig:
            if (!zks.isReconfigEnabled()) {
                LOG.error("Reconfig operation requested but reconfig feature is disabled.");
                throw new KeeperException.ReconfigDisabledException();
            }
            LOG.error("ZKBridge doesn't support reconfig operation!");
            break;
        case OpCode.setACL:
            zks.sessionTracker.checkSession(request.sessionId, request.getOwner());
            SetACLRequest setAclRequest = (SetACLRequest) record;
            if (deserialize) {
                ByteBufferInputStream.byteBuffer2Record(request.request, setAclRequest);
            }
            path = setAclRequest.getPath();
            validatePath(path, request.sessionId);
            List<ACL> listACL = fixupACL(path, request.authInfo, setAclRequest.getAcl());
            nodeRecord = getRecordForPath(path);
            zks.checkACL(request.cnxn, nodeRecord.acl, ZooDefs.Perms.ADMIN, request.authInfo, path, listACL);
            newVersion = checkAndIncVersion(nodeRecord.stat.getAversion(), setAclRequest.getVersion(), path);
            request.setTxn(new SetACLTxn(path, listACL, newVersion));
            nodeRecord = nodeRecord.duplicate(request.getHdr().getZxid());
            nodeRecord.stat.setAversion(newVersion);
            nodeRecord.precalculatedDigest = precalculateDigest(
                    DigestOpCode.UPDATE, path, nodeRecord.data, nodeRecord.stat);
            setTxnDigest(request, nodeRecord.precalculatedDigest);
            addChangeRecord(nodeRecord);
            break;
        case OpCode.createSession:
            request.request.rewind();
            int to = request.request.getInt();
            request.setTxn(new CreateSessionTxn(to));
            request.request.rewind();
            // only add the global session tracker but not to ZKDb
            zks.sessionTracker.trackSession(request.sessionId, to);
            zks.setOwner(request.sessionId, request.getOwner());
            break;
        case OpCode.closeSession:
            // We don't want to do this check since the session expiration thread
            // queues up this operation without being the session owner.
            // this request is the last of the session so it should be ok
            //zks.sessionTracker.checkSession(request.sessionId, request.getOwner());
            long startTime = Time.currentElapsedTime();
            synchronized (zks.outstandingChanges) {
                // need to move getEphemerals into zks.outstandingChanges
                // synchronized block, otherwise there will be a race
                // condition with the on flying deleteNode txn, and we'll
                // delete the node again here, which is not correct
                Set<String> es = zks.getZKDatabase().getEphemerals(request.sessionId);
                for (ChangeRecord c : zks.outstandingChanges) {
                    if (c.stat == null) {
                        // Doing a delete
                        es.remove(c.path);
                    } else if (c.stat.getEphemeralOwner() == request.sessionId) {
                        es.add(c.path);
                    }
                }
                for (String path2Delete : es) {
                    if (digestEnabled) {
                        parentPath = getParentPathAndValidate(path2Delete);
                        parentRecord = getRecordForPath(parentPath);
                        parentRecord = parentRecord.duplicate(request.getHdr().getZxid());
                        parentRecord.stat.setPzxid(request.getHdr().getZxid());
                        parentRecord.precalculatedDigest = precalculateDigest(
                                DigestOpCode.UPDATE, parentPath, parentRecord.data, parentRecord.stat);
                        addChangeRecord(parentRecord);
                    }
                    nodeRecord = new ChangeRecord(
                            request.getHdr().getZxid(), path2Delete, null, 0, null);
                    nodeRecord.precalculatedDigest = precalculateDigest(
                            DigestOpCode.REMOVE, path2Delete);
                    addChangeRecord(nodeRecord);
                }
                if (ZooKeeperServer.isCloseSessionTxnEnabled()) {
                    request.setTxn(new CloseSessionTxn(new ArrayList<String>(es)));
                }
                zks.sessionTracker.setSessionClosing(request.sessionId);
            }
            ServerMetrics.getMetrics().CLOSE_SESSION_PREP_TIME.add(Time.currentElapsedTime() - startTime);
            break;
        case OpCode.check:
            zks.sessionTracker.checkSession(request.sessionId, request.getOwner());
            CheckVersionRequest checkVersionRequest = (CheckVersionRequest) record;
            if (deserialize) {
                ByteBufferInputStream.byteBuffer2Record(request.request, checkVersionRequest);
            }
            path = checkVersionRequest.getPath();
            validatePath(path, request.sessionId);
            nodeRecord = getRecordForPath(path);
            zks.checkACL(request.cnxn, nodeRecord.acl, ZooDefs.Perms.READ, request.authInfo, path, null);
            request.setTxn(new CheckVersionTxn(
                path,
                checkAndIncVersion(nodeRecord.stat.getVersion(), checkVersionRequest.getVersion(), path)));
            break;
        default:
            LOG.warn("unknown type {}", type);
            break;
        }

        // If the txn is not going to mutate anything, like createSession,
        // we just set the current tree digest in it
        if (request.getTxnDigest() == null && digestEnabled) {
            setTxnDigest(request);
        }
    }

    private void pRequest2TxnCreate(int type, Request request, Record record, boolean deserialize) throws IOException, KeeperException {
        if (deserialize) {
            ByteBufferInputStream.byteBuffer2Record(request.request, record);
        }

        int flags;
        String path;
        List<ACL> acl;
        byte[] data;
        long ttl;
        if (type == OpCode.createTTL) {
            CreateTTLRequest createTtlRequest = (CreateTTLRequest) record;
            flags = createTtlRequest.getFlags();
            path = createTtlRequest.getPath();
            acl = createTtlRequest.getAcl();
            data = createTtlRequest.getData();
            ttl = createTtlRequest.getTtl();
        } else {
            CreateRequest createRequest = (CreateRequest) record;
            flags = createRequest.getFlags();
            path = createRequest.getPath();
            acl = createRequest.getAcl();
            data = createRequest.getData();
            ttl = -1;
        }
        CreateMode createMode = CreateMode.fromFlag(flags);
        validateCreateRequest(path, createMode, request, ttl);
        String parentPath = validatePathForCreate(path, request.sessionId);

        List<ACL> listACL = fixupACL(path, request.authInfo, acl);
        ChangeRecord parentRecord = getRecordForPath(parentPath);

        zks.checkACL(request.cnxn, parentRecord.acl, ZooDefs.Perms.CREATE, request.authInfo, path,
            listACL);
        int parentCVersion = parentRecord.stat.getCversion();
        if (createMode.isSequential()) {
            path = path + String.format(Locale.ENGLISH, "%010d", parentCVersion);
        }
        validatePath(path, request.sessionId);

        boolean ephemeralParent = EphemeralType.get(parentRecord.stat.getEphemeralOwner()) == EphemeralType.NORMAL;
        if (ephemeralParent) {
            throw new KeeperException.NoChildrenForEphemeralsException(path);
        }
        int newCversion = parentRecord.stat.getCversion() + 1;
        if (type == OpCode.createContainer) {
            request.setTxn(new CreateContainerTxn(path, data, listACL, newCversion));
        } else if (type == OpCode.createTTL) {
            request.setTxn(new CreateTTLTxn(path, data, listACL, newCversion, ttl));
        } else {
            request.setTxn(new CreateTxn(path, data, listACL, createMode.isEphemeral(), newCversion));
        }
        TxnHeader hdr = request.getHdr();
        long ephemeralOwner = 0;
        if (createMode.isContainer()) {
            ephemeralOwner = EphemeralType.CONTAINER_EPHEMERAL_OWNER;
        } else if (createMode.isTTL()) {
            ephemeralOwner = EphemeralType.TTL.toEphemeralOwner(ttl);
        } else if (createMode.isEphemeral()) {
            ephemeralOwner = request.sessionId;
        }
        StatPersisted s = DataTree.createStat(hdr.getZxid(), hdr.getTime(), ephemeralOwner);
        parentRecord = parentRecord.duplicate(request.getHdr().getZxid());
        parentRecord.childCount++;
        parentRecord.stat.setCversion(newCversion);
        parentRecord.stat.setPzxid(request.getHdr().getZxid());
        parentRecord.precalculatedDigest =
            precalculateDigest(DigestOpCode.UPDATE, parentPath, parentRecord.data, parentRecord.stat);
        addChangeRecord(parentRecord);
        SpiralNode parent = SpiralNode.convert2Spiral(parentRecord);
        try {
            zks.createSpiralRecord(parentPath, parent);
        } catch (Exception e) {
            // handle exception
        }
        ChangeRecord nodeRecord = new ChangeRecord(request.getHdr().getZxid(), path, s, 0, listACL);
        nodeRecord.data = data;
        nodeRecord.precalculatedDigest = precalculateDigest(DigestOpCode.ADD, path, nodeRecord.data, s);
        setTxnDigest(request, nodeRecord.precalculatedDigest);
        addChangeRecord(nodeRecord);
        SpiralNode spiral = new SpiralNode(data, 0L, s);
        try {
            zks.createSpiralRecord(path, spiral);
        } catch (IOException e) {
            // handle exception
        }
    }



    private void validatePath(String path, long sessionId) throws BadArgumentsException {
        try {
            PathUtils.validatePath(path);
        } catch (IllegalArgumentException ie) {
            LOG.info("Invalid path {} with session 0x{}, reason: {}", path, Long.toHexString(sessionId), ie.getMessage());
            throw new BadArgumentsException(path);
        }
    }

    private String getParentPathAndValidate(String path) throws BadArgumentsException {
        int lastSlash = path.lastIndexOf('/');
        if (lastSlash == -1 || path.indexOf('\0') != -1 || zks.getZKDatabase().isSpecialPath(path)) {
            throw new BadArgumentsException(path);
        }
        return path.substring(0, lastSlash);
    }

    private static int checkAndIncVersion(int currentVersion, int expectedVersion, String path) throws KeeperException.BadVersionException {
        if (expectedVersion != -1 && expectedVersion != currentVersion) {
            throw new KeeperException.BadVersionException(path);
        }
        return currentVersion + 1;
    }

    /**
     * This method will be called inside the ProcessRequestThread, which is a
     * singleton, so there will be a single thread calling this code.
     *
     * @param request
     */
    protected void pRequest(Request request) throws RequestProcessorException {
        // LOG.info("Prep>>> cxid = " + request.cxid + " type = " +
        // request.type + " id = 0x" + Long.toHexString(request.sessionId));
        request.setHdr(null);
        request.setTxn(null);

        try {
            switch (request.type) {
            case OpCode.createContainer:
            case OpCode.create:
            case OpCode.create2:
                CreateRequest create2Request = new CreateRequest();
                pRequest2Txn(request.type, zks.getNextZxid(), request, create2Request, true);
                break;
            case OpCode.createTTL:
                CreateTTLRequest createTtlRequest = new CreateTTLRequest();
                pRequest2Txn(request.type, zks.getNextZxid(), request, createTtlRequest, true);
                break;
            case OpCode.deleteContainer:
            case OpCode.delete:
                DeleteRequest deleteRequest = new DeleteRequest();
                pRequest2Txn(request.type, zks.getNextZxid(), request, deleteRequest, true);
                break;
            case OpCode.setData:
                SetDataRequest setDataRequest = new SetDataRequest();
                pRequest2Txn(request.type, zks.getNextZxid(), request, setDataRequest, true);
                break;
            case OpCode.reconfig:
                ReconfigRequest reconfigRequest = new ReconfigRequest();
                ByteBufferInputStream.byteBuffer2Record(request.request, reconfigRequest);
                pRequest2Txn(request.type, zks.getNextZxid(), request, reconfigRequest, true);
                break;
            case OpCode.setACL:
                SetACLRequest setAclRequest = new SetACLRequest();
                pRequest2Txn(request.type, zks.getNextZxid(), request, setAclRequest, true);
                break;
            case OpCode.check:
                CheckVersionRequest checkRequest = new CheckVersionRequest();
                pRequest2Txn(request.type, zks.getNextZxid(), request, checkRequest, true);
                break;
            case OpCode.multi:
                // TODO - Spiral Txn
                LOG.error("ZKBridge - Txn support is not yet available");
                break;

            //create/close session don't require request record
            case OpCode.createSession:
            case OpCode.closeSession:
                // TODO - Spiral Session support
                if (!request.isLocalSession()) {
                    pRequest2Txn(request.type, zks.getNextZxid(), request, null, true);
                }
                break;

            //All the rest don't need to create a Txn - just verify session
            case OpCode.sync:
            case OpCode.exists:
            case OpCode.getData:
            case OpCode.getACL:
            case OpCode.getChildren:
            case OpCode.getAllChildrenNumber:
            case OpCode.getChildren2:
            case OpCode.getChildrenPaginated:
            case OpCode.ping:
            case OpCode.setWatches:
            case OpCode.setWatches2:
            case OpCode.checkWatches:
            case OpCode.removeWatches:
            case OpCode.getEphemerals:
            case OpCode.multiRead:
            case OpCode.addWatch:
                zks.sessionTracker.checkSession(request.sessionId, request.getOwner());
                break;
            default:
                LOG.warn("unknown type {}", request.type);
                break;
            }
        } catch (KeeperException e) {
            if (request.getHdr() != null) {
                request.getHdr().setType(OpCode.error);
                request.setTxn(new ErrorTxn(e.code().intValue()));
            }

            if (e.code().intValue() > Code.APIERROR.intValue()) {
                LOG.info(
                    "Got user-level KeeperException when processing {} Error Path:{} Error:{}",
                    request.toString(),
                    e.getPath(),
                    e.getMessage());
            }
            request.setException(e);
        } catch (Exception e) {
            // log at error level as we are returning a marshalling
            // error to the user
            LOG.error("Failed to process {}", request, e);

            StringBuilder sb = new StringBuilder();
            ByteBuffer bb = request.request;
            if (bb != null) {
                bb.rewind();
                while (bb.hasRemaining()) {
                    sb.append(Integer.toHexString(bb.get() & 0xff));
                }
            } else {
                sb.append("request buffer is null");
            }

            LOG.error("Dumping request buffer: 0x{}", sb.toString());
            if (request.getHdr() != null) {
                request.getHdr().setType(OpCode.error);
                request.setTxn(new ErrorTxn(Code.MARSHALLINGERROR.intValue()));
            }
        }
        request.zxid = zks.getZxid();
        ServerMetrics.getMetrics().PREP_PROCESS_TIME.add(Time.currentElapsedTime() - request.prepStartTime);
        nextProcessor.processRequest(request);
    }

    // This is LinkedIn specific
    // TODO - do we need to support ACL through ZKBridge?
    private static List<ACL> removeDuplicates(final List<ACL> acls) {
        if (acls == null || acls.isEmpty()) {
            return Collections.emptyList();
        }

        // This would be done better with a Set but ACL hashcode/equals do not
        // allow for null values
        final ArrayList<ACL> retval = new ArrayList<>(acls.size());
        for (final ACL acl : acls) {
            if (!retval.contains(acl)) {
                retval.add(acl);
            }
        }
        return retval;
    }

    private void validateCreateRequest(String path, CreateMode createMode, Request request, long ttl) throws KeeperException {
        if (createMode.isTTL() && !EphemeralType.extendedEphemeralTypesEnabled()) {
            throw new KeeperException.UnimplementedException();
        }
        try {
            EphemeralType.validateTTL(createMode, ttl);
        } catch (IllegalArgumentException e) {
            throw new BadArgumentsException(path);
        }
        if (createMode.isEphemeral()) {
            // Exception is set when local session failed to upgrade
            // so we just need to report the error
            if (request.getException() != null) {
                throw request.getException();
            }
            zks.sessionTracker.checkGlobalSession(request.sessionId, request.getOwner());
        } else {
            zks.sessionTracker.checkSession(request.sessionId, request.getOwner());
        }
    }

    /**
     * This method checks out the acl making sure it isn't null or empty,
     * it has valid schemes and ids, and expanding any relative ids that
     * depend on the requestor's authentication information.
     *
     * @param authInfo list of ACL IDs associated with the client connection
     * @param acls list of ACLs being assigned to the node (create or setACL operation)
     * @return verified and expanded ACLs
     * @throws KeeperException.InvalidACLException
     */
    // LinkedIn specific - do we need this for ZKBridge?
    public static List<ACL> fixupACL(String path, List<Id> authInfo, List<ACL> acls) throws KeeperException.InvalidACLException {
        // check for well formed ACLs
        // This resolves https://issues.apache.org/jira/browse/ZOOKEEPER-1877
        List<ACL> uniqacls = removeDuplicates(acls);
        if (uniqacls == null || uniqacls.size() == 0) {
            throw new KeeperException.InvalidACLException(path);
        }
        List<ACL> rv = new ArrayList<>();


        Set<String> enabledX509ClientIdAsACL = authInfo.stream()
            .filter(id -> X509AuthenticationUtil.X509_SCHEME.equals(id.getScheme()))
            .map(Id::getId)
            .collect(Collectors.toCollection(HashSet::new));
        if (!X509AuthenticationConfig.getInstance().isX509ClientIdAsAclEnabled()) {
          enabledX509ClientIdAsACL
              .removeIf(id -> !X509AuthenticationConfig.getInstance().getAllowedClientIdAsAclDomains().contains(id));
        }

      if ((!enabledX509ClientIdAsACL.isEmpty() || X509AuthenticationConfig.getInstance().isX509ClientIdAsAclEnabled())
            && X509AuthenticationConfig.getInstance().isX509ZnodeGroupAclEnabled()
            && !X509AuthenticationConfig.getInstance().isZnodeGroupAclDedicatedServerEnabled()) {
            boolean isUserProvidedAclOverriden = false;
            for (Id id : authInfo) {
                boolean isX509 = enabledX509ClientIdAsACL.contains(id.getId());
                boolean isX509CrossDomainComponent =
                    id.getScheme().equals(X509AuthenticationUtil.SUPERUSER_AUTH_SCHEME)
                        && !X509AuthenticationConfig.getInstance().getZnodeGroupAclSuperUserIds()
                        .contains(id.getId());
                if (isX509 || isX509CrossDomainComponent) {
                    rv.add(new ACL(ZooDefs.Perms.ALL,
                        new Id(X509AuthenticationUtil.X509_SCHEME, id.getId())));
                    isUserProvidedAclOverriden = true;
                }
            }
            // If the znode path contains open read access node path prefix, add (world:anyone, r)
            if (X509AuthenticationConfig.getInstance().getZnodeGroupAclOpenReadAccessPathPrefixes()
                .stream().anyMatch(path::startsWith)) {
                rv.add(new ACL(ZooDefs.Perms.READ, ZooDefs.Ids.ANYONE_ID_UNSAFE));
            }
            if (isUserProvidedAclOverriden) {
                // Only for users who are handled by the above logic, return the result,
                // for others should continue to original fixupACL logic. This variable is necessary
                // because if path is open read path, its open read ACL will be added to the list,
                // regardless of user category, so rv's size won't be a good indicator here.
                return rv;
            }
        }

        for (ACL a : uniqacls) {
            LOG.debug("Processing ACL: {}", a);
            if (a == null) {
                throw new KeeperException.InvalidACLException(path);
            }
            Id id = a.getId();
            if (id == null || id.getScheme() == null) {
                throw new KeeperException.InvalidACLException(path);
            }
            if (id.getScheme().equals("world") && id.getId().equals("anyone")) {
                rv.add(a);
            } else if (id.getScheme().equals("auth")) {
                // This is the "auth" id, so we have to expand it to the
                // authenticated ids of the requestor
                boolean authIdValid = false;
                for (Id cid : authInfo) {
                    ServerAuthenticationProvider ap = ProviderRegistry.getServerProvider(cid.getScheme());
                    if (ap == null) {
                        LOG.error("Missing AuthenticationProvider for {}", cid.getScheme());
                    } else if (ap.isAuthenticated()) {
                        authIdValid = true;
                        rv.add(new ACL(a.getPerms(), cid));
                    }
                }
                if (!authIdValid) {
                    throw new KeeperException.InvalidACLException(path);
                }
            } else {
                ServerAuthenticationProvider ap = ProviderRegistry.getServerProvider(id.getScheme());
                if (ap == null || !ap.isValid(id.getId())) {
                    throw new KeeperException.InvalidACLException(path);
                }
                rv.add(a);
            }
        }
        return rv;
    }

    public void processRequest(Request request) {
        request.prepQueueStartTime = Time.currentElapsedTime();
        submittedRequests.add(request);
        ServerMetrics.getMetrics().PREP_PROCESSOR_QUEUED.add(1);
    }

    public void shutdown() {
        LOG.info("Shutting down");
        submittedRequests.clear();
        submittedRequests.add(Request.requestOfDeath);
        nextProcessor.shutdown();
    }

    // For ZKBridge, digest is disabled.
    private PrecalculatedDigest precalculateDigest(DigestOpCode type, String path,
            byte[] data, StatPersisted s) throws KeeperException.NoNodeException {

        if (!digestEnabled) {
            return null;
        }
        return null; // for zkbridge, digest is not supported.
    }

    private PrecalculatedDigest precalculateDigest(
            DigestOpCode type, String path) throws KeeperException.NoNodeException {
        return precalculateDigest(type, path, null, null);
    }

    private long getCurrentTreeDigest() {
        return 0L;
    }

    private void setTxnDigest(Request request) {
        // NoOP
    }

    private void setTxnDigest(Request request, PrecalculatedDigest preCalculatedDigest) {
        // NoOP
    }

    public SpiralNode convert2Spiral(Request request) throws IOException {
        CreateRequest createRequest = new CreateRequest();
        ByteBufferInputStream.byteBuffer2Record(request.request, createRequest);

        // TODO - convert lit of ACL to some long?
        long acl = 0;
        byte[] data = createRequest.getData();
        StatPersisted stat = new StatPersisted();
        stat.setCtime(request.getHdr().getTime());
        stat.setMtime(request.getHdr().getTime());
        stat.setCzxid(request.getHdr().getZxid());
        stat.setMzxid(request.getHdr().getZxid());
        stat.setPzxid(request.getHdr().getZxid());
        stat.setVersion(0);
        stat.setAversion(0);
        stat.setEphemeralOwner(0);
        return new SpiralNode(data, acl, stat);
    }
}

