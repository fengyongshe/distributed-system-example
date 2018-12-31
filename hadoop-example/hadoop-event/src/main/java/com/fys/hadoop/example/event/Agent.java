package com.fys.hadoop.example.event;

import org.apache.hadoop.yarn.event.EventHandler;

import java.util.HashSet;
import java.util.Set;

public class Agent implements EventHandler<AgentEvent> {

  private final int id;
  private Set<Long> runningJobs = new HashSet<Long>();

  public Agent(int id) {
    this.id = id;
  }

  @Override
  public int hashCode(){
    return id;
  }

  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj == null) {
      return false;
    } else if (this.getClass() != obj.getClass()) {
      return false;
    } else {
      Agent other = (Agent) obj;
      return this.id == other.id;
    }
  }

  public int getId() {
    return id;
  }

  @Override
  public String toString() {
    return "Agent: " + id;
  }

  public void handle(AgentEvent agentEvent) {

    switch (agentEvent.getType()) {
      case REGISTER:
        System.out.println("Receive Register event in agent");
        break;
      case UNREGISTER:
        break;
      case EXPIRED:
        break;
      case JOBLAUNCH:
        JobEvent jobEvent = (JobEvent)agentEvent;
        System.out.println("agent:" +  agentEvent.getAgent()
            +" receive JOBLAUNCH event, jobId :" + jobEvent.getJobId());

        break;
      case JOBKILL:
        break;
    }
  }
}
