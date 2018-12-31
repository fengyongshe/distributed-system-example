package com.fys.helix.cluster;

import org.apache.helix.manager.zk.ZKHelixAdmin;
import org.apache.helix.model.InstanceConfig;

public class TestConfigNodes {

  public static void main(String[] args) {
    String CLUSTER_NAME = "helix-demo";
    int NUM_NODES = 2;
    String hosts[] = new String[]{"fys1.cmss.com","fys1.cmss.com"};
    String ports[] = new String[]{"7000","7001"};

    String ZK_ADDRESS = "fys1.cmss.com:2181";
    ZKHelixAdmin admin = new ZKHelixAdmin(ZK_ADDRESS);

    for(int i = 0;i< NUM_NODES;i++) {
      InstanceConfig instanceConfig = new InstanceConfig(hosts[i]+"_"+ports[i]);
      instanceConfig.setHostName(hosts[i]);
      instanceConfig.setPort(ports[i]);
      instanceConfig.setInstanceEnabled(true);

      instanceConfig.getRecord().setSimpleField("key","value");
      admin.addInstance(CLUSTER_NAME,instanceConfig);
    }
  }
}
