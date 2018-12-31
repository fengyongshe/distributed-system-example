package com.fys.ratis.example.arithmetic.cli;

import com.beust.jcommander.Parameter;
import org.apache.ratis.protocol.RaftPeer;
import org.apache.ratis.protocol.RaftPeerId;

import java.util.stream.Stream;

public abstract class SubCommandBase {

  @Parameter(names = {"--raftGroup", "-g"}, description = "Raft group identifier")
  protected String raftGroupId = "demoRaftGroup123";

  @Parameter(names = {"--peers", "-r"}, description = "Raft peers (format: name:host:port,"
      + "name:host:port)", required = true)
  protected String peers;

  public static RaftPeer[] parsePeers(String peers) {
    return Stream.of(peers.split(","))
        .map(
            address -> {
                String[] addressParts = address.split(":");
                return new RaftPeer(RaftPeerId.valueOf(addressParts[0]),
              addressParts[1] + ":" + addressParts[2]);
            }
        )
        .toArray(RaftPeer[]::new);
  }

  public RaftPeer[] getPeers() {
    return parsePeers(peers);
  }

  public abstract void run() throws Exception;

}
