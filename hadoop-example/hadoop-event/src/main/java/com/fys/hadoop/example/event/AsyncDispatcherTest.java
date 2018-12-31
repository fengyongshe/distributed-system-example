package com.fys.hadoop.example.event;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.event.AsyncDispatcher;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class AsyncDispatcherTest {

  @Test
  public void asyncDispatcherTest1() {
    AgentManager agentManager = new AgentManager();
    Configuration conf = new Configuration();
    AsyncDispatcher dispatcher = new AsyncDispatcher();
    dispatcher.init(conf);
    dispatcher.register(AgentEventType.class, agentManager );
    dispatcher.start();
    dispatcher.getEventHandler().handle(
        new AgentEvent(AgentEventType.REGISTER, new Agent(1)));
    System.out.println("send register request in asyncDispatcherTest1");

    dispatcher.getEventHandler().handle(
        new JobEvent(AgentEventType.JOBLAUNCH, new Agent(1), 2));
    System.out.println("send job launch request in asyncDispatcherTest1");
    try {
      TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    dispatcher.stop();
  }


}
