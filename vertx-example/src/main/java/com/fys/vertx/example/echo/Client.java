package com.fys.vertx.example.echo;

import com.fys.vertx.example.p2p.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.NetSocket;

public class Client extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(Client.class);
  }

  @Override
  public void start() throws Exception {

    vertx.createNetClient().connect(1234, "cmhhost3.novalocal",
        res -> {
          if (res.succeeded()) {
            NetSocket socket = res.result();
            socket.handler(
                buffer -> {
                  System.out.println("Net Client receiving: " + buffer.toString("UTF-8"));
                }
            );
            for( int i = 0 ;i < 10 ;i ++) {
              String str = "hello "+ i + "\n";
              System.out.println("Net Client Sending: " + str);
              socket.write(str);
            }
          } else {
            System.out.println("Failed to connect " + res.cause());
          }
        }
     );
  }
}
