package com.fys.vertx.example.http;

import com.fys.vertx.example.p2p.Runner;
import io.vertx.core.AbstractVerticle;

public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  @Override
  public void start() throws Exception {
    vertx.createHttpServer().requestHandler(
        req -> {
          req.response().putHeader("content-type","text/html")
              .end("<html><body><h1>Hello from vert.x!</h1></body></html>");
        }
    ).listen(9990);
  }
}
