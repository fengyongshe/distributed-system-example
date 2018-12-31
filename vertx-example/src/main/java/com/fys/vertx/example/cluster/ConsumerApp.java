package com.fys.vertx.example.cluster;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.shareddata.AsyncMap;
import io.vertx.core.shareddata.SharedData;

import java.util.List;

public class ConsumerApp extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.clusteredVertx(new VertxOptions().setClustered(true), ar -> {
      if (ar.failed()) {
        System.err.println("Cannot create vert.x intance : " + ar.cause());
      } else {
        Vertx vertx = ar.result();
        vertx.deployVerticle(ConsumerApp.class.getName());
      }
    });
  }

  @Override
  public void start() throws Exception {

    SharedData sd = vertx.sharedData();
    sd.<String,String>getClusterWideMap("mymap", res -> {
      System.out.println("Get SharedData for myApp");
      if(res.succeeded()) {
        AsyncMap<String,String> myMap = res.result();
        myMap.values(
            list -> {
              List<String> result = list.result();
              for(String str: result) {
                System.out.println("The result in mymap : " + str);
              }
            }
        );
      } else {
        System.out.println("Something went wrong !!!");
      }
    });
  }
}
