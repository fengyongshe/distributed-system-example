package com.fys.vertx.example.cluster;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.shareddata.AsyncMap;
import io.vertx.core.shareddata.SharedData;

public class ProduerApp extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.clusteredVertx(new VertxOptions().setClustered(true), ar -> {
      if (ar.failed()) {
        System.err.println("Cannot create vert.x instance : " + ar.cause());
      } else {
        Vertx vertx = ar.result();
        vertx.deployVerticle(ProduerApp.class.getName());
      }
    });
  }

  @Override
  public void start() throws Exception {

    SharedData sd = vertx.sharedData();
     sd.<String,String>getClusterWideMap("mymap", res -> {
      if(res.succeeded()) {
        AsyncMap<String,String> mymap = res.result();
        System.out.println("Put data into mymap!!!");
        mymap.put("foo","value", result -> {
          if(result.succeeded()) {
            System.out.println("Data puted successed!1");
          }
        });
      } else {
        System.out.println("Something went wrong!");
      }
    });

  }

}
