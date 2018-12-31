package com.fys.aop.agentmain;

import com.sun.tools.attach.VirtualMachine;

public class RunAttach {

  public static void main(String[] args) throws Exception {
    String targetPid = "6787";
    VirtualMachine vm = VirtualMachine.attach(targetPid);
    vm.loadAgent("/root/workspace/distributed-system/aop/target/aop-1.0-SNAPSHOT.jar","toagent");
  }

}
