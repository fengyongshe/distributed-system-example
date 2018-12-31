package com.fys.vertx.example.p2p;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

public class Sender extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runClusteredExample(Sender.class);
  }

  @Override
  public void start() throws Exception {
    EventBus bus = vertx.eventBus();
    System.out.println("Sender Ready, Start to receive reply!");

    vertx.setPeriodic(100, v -> {

      bus.send("ping-address", "ping!", reply -> {
        if(reply.succeeded()) {
          System.out.println("Received reply " + reply.result().body());
        } else {
          System.out.println("No reply");
        }
      });
    });
  }

}
