package com.fys.ratis.example.arithmetic.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.fys.ratis.example.arithmetic.ArithmeticStateMachine;
import org.apache.ratis.conf.RaftProperties;
import org.apache.ratis.grpc.GrpcConfigKeys;
import org.apache.ratis.protocol.RaftGroup;
import org.apache.ratis.protocol.RaftGroupId;
import org.apache.ratis.protocol.RaftPeer;
import org.apache.ratis.protocol.RaftPeerId;
import org.apache.ratis.server.RaftServer;
import org.apache.ratis.server.RaftServerConfigKeys;
import org.apache.ratis.shaded.com.google.protobuf.ByteString;
import org.apache.ratis.statemachine.StateMachine;
import org.apache.ratis.util.NetUtils;

import java.io.File;
import java.util.Objects;

@Parameters(commandDescription = "Start an arithmetic service")
public class Server extends SubCommandBase {

  @Parameter(names = {"--id","-i"} , description = "Raft id of this server", required = true)
  private String id;

  @Parameter(names = {"--storage", "-s"}, description = "Storage dir", required = true)
  private File storageDir;

  @Override
  public void run() throws Exception {
    RaftPeerId peerId = RaftPeerId.valueOf(id);
    RaftProperties properties = new RaftProperties();

    RaftPeer[] peers = getPeers();
    final int port = NetUtils.createSocketAddr(getPeer(peerId).getAddress()).getPort();
    GrpcConfigKeys.Server.setPort(properties, port);
    properties.setInt(GrpcConfigKeys.OutputStream.RETRY_TIMES_KEY, Integer.MAX_VALUE);
    RaftServerConfigKeys.setStorageDir(properties, storageDir);

    StateMachine stateMachine = new ArithmeticStateMachine();
    RaftGroup raftGroup = new RaftGroup(RaftGroupId.valueOf(ByteString.copyFromUtf8(raftGroupId)), peers);
    RaftServer raftServer = RaftServer.newBuilder()
        .setServerId(RaftPeerId.valueOf(id))
        .setStateMachine(stateMachine)
        .setProperties(properties)
        .setGroup(raftGroup)
        .build();
    raftServer.start();
  }

  /**
   * @return the peer with the given id if it is in this group; otherwise, return null.
   */
  public RaftPeer getPeer(RaftPeerId id) {
    Objects.requireNonNull(id, "id == null");
    for (RaftPeer p : getPeers()) {
      if (id.equals(p.getId())) {
        return p;
      }
    }
    throw new IllegalArgumentException("Raft peer id " + id + " is not part of the raft group definitions " + peers);
  }

}
