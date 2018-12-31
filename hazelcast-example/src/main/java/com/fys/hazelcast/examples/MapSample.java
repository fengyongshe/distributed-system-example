package com.fys.hazelcast.examples;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;

public class MapSample {

  public static void main(String[] args) {

    ClientConfig clientConfig = new ClientConfig();
    clientConfig.getNetworkConfig().addAddress("cmhhost1.novalocal:5701");
    HazelcastInstance hz = HazelcastClient.newHazelcastClient(clientConfig);
    IExecutorService executor = hz.getExecutorService("Executor");

    executor.execute(new EchoTask("Echo Task to Execute"));
    hz.shutdown();
  }

}
