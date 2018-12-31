package com.fys.hazelcast.examples;

import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.pipeline.Pipeline;
import com.hazelcast.jet.pipeline.Sinks;
import com.hazelcast.jet.pipeline.Sources;

import java.util.List;
import java.util.Map;

import static com.hazelcast.jet.Traversers.traverseArray;
import static com.hazelcast.jet.aggregate.AggregateOperations.counting;
import static com.hazelcast.jet.function.DistributedFunctions.wholeItem;


public class HelloJet {

  public static void main(String[] args) {

    Pipeline p  = Pipeline.create();
    p.drawFrom(Sources.<String>list("text"))
        .flatMap(word -> traverseArray(word.toLowerCase().split("\\W+")))
        .filter(word -> !word.isEmpty())
        .groupingKey(wholeItem())
        .aggregate(counting())
        .drainTo(Sinks.map("counts"));

    JetInstance jet = Jet.newJetInstance();

    try {
      List<String> text = jet.getList("text");
      text.add("Hello world hello hello world");
      text.add("world world hello world");
      jet.newJob(p).join();
      Map<String,Long> counts = jet.getMap("counts");
      System.out.println("Count of hello: " + counts.get("hello"));
      System.out.println("Count of world: " + counts.get("world"));
    } finally {
      Jet.shutdownAll();
    }

  }
}
