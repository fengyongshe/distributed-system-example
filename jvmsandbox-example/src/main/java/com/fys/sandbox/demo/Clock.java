package com.fys.sandbox.demo;

import java.util.Date;

public abstract class Clock {

  abstract void checkState();
  private final java.text.SimpleDateFormat clockDateFormat =
      new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  final String formatDate(Date date) {
    return clockDateFormat.format(date);
  }

  final Date nowDate() {
    return new Date();
  }

  final String report() {
    checkState();
    return formatDate(nowDate());
  }

  abstract void delay() throws InterruptedException;

  final void loopReport() throws InterruptedException {
    while(true) {
      try {
        System.out.println(report());
      } catch(Throwable cause) {
        cause.printStackTrace();
      }
      delay();
    }
  }

}
