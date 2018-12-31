package com.fys.hadoop.example.event;

import org.apache.hadoop.yarn.event.AbstractEvent;

public class AgentEvent extends AbstractEvent<AgentEventType> {

  private Agent agent;

  public AgentEvent(AgentEventType agentEventType, Agent agent) {
    super(agentEventType);
    this.agent = agent;
  }

  public Agent getAgent() {
    return agent;
  }

}
