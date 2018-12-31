package com.fys.vertx.example.p2p;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

public class Receiver extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runClusteredExample(Receiver.class);
  }

  @Override
  public void start() throws Exception {
    EventBus bus = vertx.eventBus();
    bus.consumer("ping-address", message -> {
      System.out.println("Received message: " + message.body());
      message.reply("pong!");
    });
    System.out.println("Receiver ready!");
  }

}
