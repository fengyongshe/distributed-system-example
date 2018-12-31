package com.fys.vertx.example.message;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

public class LocalReceiver extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    EventBus eventBus = getVertx().eventBus();
    eventBus.consumer("local-message-receiver", message -> {
      CustomMessage customMessage = (CustomMessage) message.body();
      System.out.println("Custom message received: "+ customMessage.getSummary());
      CustomMessage replyMessage = new CustomMessage(200, "a00000002","Messae sent from local receiver!");
      message.reply(replyMessage);
    });
  }

}
