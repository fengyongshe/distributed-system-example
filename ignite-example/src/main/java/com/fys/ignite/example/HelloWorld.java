package com.fys.ignite.example;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;

public class HelloWorld {

  public static void main(String[] args) {

    try( Ignite ignite = Ignition.start("example-ignite.xml")) {

      IgniteCache<Integer,String> cache = ignite.getOrCreateCache("myCache");
      cache.put(1,"Hello");
      cache.put(2,"World!");

      ignite.compute().broadcast(() -> {

        String hello = cache.get(1);
        String world = cache.get(2);

        System.out.println(hello + " " + world);
      });
    }
  }
}
