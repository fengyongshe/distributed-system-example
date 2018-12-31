package com.fys.aop.agentmain;

import com.fys.aop.premain.TransClass;

public class TestAgentMainInJar {

  public static void main(String[] args) throws InterruptedException {
    System.out.println("Before Msg: " + new TransClass().getNumber());
    int count = 0;
    while(true) {
      Thread.sleep(1000);
      count++;
      int number = new TransClass().getNumber();
      System.out.println("Count-"+count+" Msg: "+ number);
    }
  }

}
