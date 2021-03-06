package com.fys.ignite.example.app;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.lang.IgniteCallable;

import java.util.ArrayList;
import java.util.Collection;

public class TestCompute {

  public static void main(String[] args) {
    try(Ignite ignite = Ignition.start("example-ignite.xml")) {

      Collection<IgniteCallable<Integer>> calls = new ArrayList<>();
      for(final String word : "Count characters using callable".split(" ")) {
        calls.add(word ::length);
      }
      Collection<Integer> res = ignite.compute().call(calls);
      int sum = res.stream().mapToInt(Integer::intValue).sum();
      System.out.println("Total Number of character is :" + sum);
    }
  }
}
