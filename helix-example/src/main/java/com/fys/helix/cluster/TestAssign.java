package com.fys.helix.cluster;

import org.apache.helix.manager.zk.ZKHelixAdmin;

public class TestAssign {

  public static void main(String[] args) {
    String ZK_ADDRESS = "fys1.cmss.com:2181";
    ZKHelixAdmin admin = new ZKHelixAdmin(ZK_ADDRESS);

    String RESOURCE_NAME = "MyDB";
    int NUM_PARTITIONS = 6;
    String STATE_MODEL_NAME = "MasterSlave";
    String MODE = "SEMI_AUTO";
    int NUM_REPLICAS = 2;
    String CLUSTER_NAME = "helix-demo";

    admin.addResource(CLUSTER_NAME, RESOURCE_NAME, NUM_PARTITIONS, STATE_MODEL_NAME, MODE);
    admin.rebalance(CLUSTER_NAME, RESOURCE_NAME, NUM_REPLICAS);
  }
}
