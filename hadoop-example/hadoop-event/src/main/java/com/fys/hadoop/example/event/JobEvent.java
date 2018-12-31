package com.fys.hadoop.example.event;

public class JobEvent extends AgentEvent {

  private int jobId;

  public JobEvent(AgentEventType agentEventType, Agent agent , int jobId) {
    super(agentEventType, agent);
    this.jobId = jobId;
  }

  public int getJobId() {
    return jobId;
  }

}
