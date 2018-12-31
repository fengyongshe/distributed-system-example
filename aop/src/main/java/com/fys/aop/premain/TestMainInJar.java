package com.fys.aop.premain;

public class TestMainInJar {

  public static void main(String[] args) {
    System.out.println("Msg: "+new TransClass().getNumber());
  }

}
