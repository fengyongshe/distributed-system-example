package com.fys.aop.agentmain;

import java.lang.instrument.Instrumentation;

public class AgentAfterMain {

  public static void agentmain(String args, Instrumentation inst) {
    System.out.println("LoadAgent after main run.args="+ args);
    Class<?>[] classes = inst.getAllLoadedClasses();
    for(Class<?> cls: classes) {
      System.out.println(cls.getName());
    }
    System.out.println("Agent run completely");
  }

}
