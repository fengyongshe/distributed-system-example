package com.fys.hazelcast.examples;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.util.Map;
import java.util.Queue;

public class HazelcastStarterMaster {

  public static void main(String[] args) {

    Config config = new Config();
    config.getManagementCenterConfig().setEnabled(true);
    config.getManagementCenterConfig().setUrl("http://10.139.4.82:8080/hazelcast-mancenter");

    HazelcastInstance instance = Hazelcast.newHazelcastInstance(config);
    Map<Integer,String> clusterMap = instance.getMap("MyMap");
    clusterMap.put(1,"Hello Hazelcast Map!");

    Queue<String> clusterQueue = instance.getQueue("MyQueue");
    clusterQueue.offer("Hello Hazelcast Queue");
    clusterQueue.offer("Hello All Hazelcasters");
  }
}
