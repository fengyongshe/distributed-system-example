package com.fys.hazelcast.examples;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

public class HazelcastMember {

  public static void main(String[] args) {

    HazelcastInstance hz = Hazelcast.newHazelcastInstance();

    IMap<String,String> mymap = hz.getMap("my-distributed-map");
    mymap.putIfAbsent("name","john");
    
  }
}
