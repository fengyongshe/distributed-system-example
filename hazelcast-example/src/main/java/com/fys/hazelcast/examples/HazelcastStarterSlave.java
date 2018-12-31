package com.fys.hazelcast.examples;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.util.Map;
import java.util.Queue;

public class HazelcastStarterSlave {

  public static void main(String[] args)  {

    Config config = new Config();
    config.getManagementCenterConfig().setEnabled(true);
    config.getManagementCenterConfig().setUrl("http://10.139.4.82:8080/hazelcast-mancenter");

    HazelcastInstance instance = Hazelcast.newHazelcastInstance(config);

    Map<Integer,String> clusterMap = instance.getMap("MyMap");
    Queue<String> clusterQueue = instance.getQueue("MyQueue");

    System.out.println("Map Value:" + clusterMap.get(1));
    System.out.println("Queue Size:" + clusterQueue.size());
    System.out.println("Queue Value:" + clusterQueue.poll());

  }
}
