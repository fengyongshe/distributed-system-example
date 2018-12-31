package com.fys.sandbox.demo;

public class BrokenClock extends Clock {

  @Override
  void checkState() {
    throw new IllegalStateException();
  }

  @Override
  void delay() throws InterruptedException {
    Thread.sleep(10000L);
  }

  public static void main(String[] args) {
    BrokenClock clock = new BrokenClock();
    try {
      clock.loopReport();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
