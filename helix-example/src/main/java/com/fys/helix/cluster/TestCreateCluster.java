package com.fys.helix.cluster;

import org.apache.helix.manager.zk.ZKHelixAdmin;

public class TestCreateCluster {

  public static void main(String[] args) {

    String ZK_ADDRESS = "fys1.cmss.com:2181";
    ZKHelixAdmin admin = new ZKHelixAdmin(ZK_ADDRESS);

    String CLUSTER_NAME = "helix-demo";
    admin.addCluster(CLUSTER_NAME);
  }
}
