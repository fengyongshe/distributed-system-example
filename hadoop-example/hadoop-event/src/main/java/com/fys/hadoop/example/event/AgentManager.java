package com.fys.hadoop.example.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.service.AbstractService;
import org.apache.hadoop.yarn.event.EventHandler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AgentManager extends AbstractService implements EventHandler<AgentEvent> {

  private static final Log LOG = LogFactory.getLog(AgentManager.class);
  private int expireInterval;
  private int monitorInterval;
  private Thread checkerThread;
  private boolean stopped = false;
  private Map<Agent, Long> running = new HashMap<Agent, Long>();

  public AgentManager() {
    super("AgentManager");
  }

  protected void serviceInit(Configuration conf) throws Exception {
    super.serviceInit(conf);
    int expireIntvl = conf.getInt("com.baidu.dscheduler.agent.expire.ms",
        1000 * 60 * 10);
    //need some sanity check
    this.expireInterval = expireIntvl;
    this.monitorInterval = expireInterval / 3;
    this.checkerThread = new Thread(new PingChecker());
    this.checkerThread.setName("Ping Checker");
  }

  protected void doStart() throws Exception {
    super.serviceStart();
    this.checkerThread.start();
  }

  protected void doStop() throws Exception {
    stopped = true;
    super.serviceStop();
    checkerThread.interrupt();
  }

  public void handle(AgentEvent agentEvent) {
    switch (agentEvent.getType()) {
      case  REGISTER:
        System.out.println("receive register request in agent manager.");
        if (!running.containsKey(agentEvent.getAgent())) {
          running.putIfAbsent(agentEvent.getAgent(), System.currentTimeMillis());
          agentEvent.getAgent().handle(agentEvent);
        }
        break;
      case UNREGISTER:
        if (running.containsKey(agentEvent.getAgent())) {
          agentEvent.getAgent().handle(agentEvent);
          running.remove(agentEvent.getAgent());
        }
        break;
      case EXPIRED:
        break;
      case HEARTBEAT:
        if (running.containsKey(agentEvent.getAgent())) {
          Agent agent = agentEvent.getAgent();
          running.put(agent, System.currentTimeMillis());
          agentEvent.getAgent().handle(agentEvent);
        }
        break;
      case JOBLAUNCH:
      case JOBKILL:
        JobEvent jobEvent = (JobEvent)agentEvent;
        System.out.println(" agent master receive "
            +" receive "+ jobEvent.getType()
            +", agent: "+ agentEvent.getAgent()
            + ", jobId :" + jobEvent.getJobId());
        if (running.containsKey(agentEvent.getAgent())) {
          Agent agent = agentEvent.getAgent();
          running.put(agent, System.currentTimeMillis());
          agentEvent.getAgent().handle(jobEvent);
        }

    }

  }

  private class PingChecker implements Runnable {
    private PingChecker() {
    }

    public void run() {
      while (!AgentManager.this.stopped && !Thread.currentThread().isInterrupted()) {


        synchronized (AgentManager.this) {
          Iterator<Map.Entry<Agent, Long>> iterator = AgentManager.this.running.entrySet().iterator();
          long currentTime = System.currentTimeMillis();

          while (true) {
            if (!iterator.hasNext()) {
              break;
            }

            Map.Entry<Agent, Long> entry = (Map.Entry) iterator.next();
            if (currentTime > ((Long) entry.getValue()).longValue() + (long) AgentManager.this.expireInterval) {
              iterator.remove();
              //AgentManager.this.expire(entry.getKey());
              AgentManager.LOG.info("Expired:" + entry.getKey().toString() + " Timed out after " + AgentManager.this.expireInterval / 1000 + " secs");
            }
          }
        }

        try {
          Thread.sleep((long) AgentManager.this.monitorInterval);
          continue;
        } catch (InterruptedException e) {
          AgentManager.this.LOG.info( AgentManager.this.getName() + " thread interrupted");
        }
      }
    }
  }


}
