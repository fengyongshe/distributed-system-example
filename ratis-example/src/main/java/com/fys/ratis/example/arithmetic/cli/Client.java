package com.fys.ratis.example.arithmetic.cli;

import org.apache.ratis.client.RaftClient;
import org.apache.ratis.conf.Parameters;
import org.apache.ratis.conf.RaftProperties;
import org.apache.ratis.grpc.GrpcFactory;
import org.apache.ratis.protocol.ClientId;
import org.apache.ratis.protocol.RaftGroup;
import org.apache.ratis.protocol.RaftGroupId;
import org.apache.ratis.shaded.com.google.protobuf.ByteString;

import java.io.IOException;

public abstract class Client extends SubCommandBase {

  @Override
  public void run() throws Exception {
    RaftProperties raftProperties = new RaftProperties();
    RaftGroup raftGroup = new RaftGroup(
        RaftGroupId.valueOf(ByteString.copyFromUtf8(raftGroupId)),
        parsePeers(peers));
    RaftClient.Builder builder =
        RaftClient.newBuilder().setProperties(raftProperties);
    builder.setRaftGroup(raftGroup);
    builder.setClientRpc(new GrpcFactory(new Parameters()).newRaftClientRpc(ClientId.randomId(), raftProperties));
    RaftClient client = builder.build();
    operation(client);
  }

  protected abstract void operation(RaftClient client) throws IOException;

}
