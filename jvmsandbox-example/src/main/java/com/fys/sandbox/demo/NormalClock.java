package com.fys.sandbox.demo;

public class NormalClock extends Clock {

  @Override
  void checkState() {
    return;
  }

  @Override
  void delay() throws InterruptedException {
    Thread.sleep(1000L);
  }

  public static void main(String[] args) {
    NormalClock clock = new NormalClock();
    try {
      clock.loopReport();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
