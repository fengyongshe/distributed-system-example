package com.fys.hazelcast.examples.container;

import com.hazelcast.spi.ManagedService;
import com.hazelcast.spi.NodeEngine;
import com.hazelcast.spi.RemoteService;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CounterService implements ManagedService, RemoteService {

  static final String NAME = "CounterService";
  Container[] containers;
  private NodeEngine nodeEngine;

  public void init(NodeEngine nodeEngine, Properties properties) {
    this.nodeEngine = nodeEngine;
    containers = new Container[nodeEngine.getPartitionService().getPartitionCount()];
    for(int i = 0 ;i < containers.length;i++) {
      containers[i] = new Container();
    }
  }

  public CounterProxy createDistributedObject(String objectName) {
    int partitionId = nodeEngine.getPartitionService().getPartitionId(objectName);
    Container container = containers[partitionId];
    container.init(objectName);
    return new CounterProxy(objectName,nodeEngine,this);
  }

  public void destroyDistributedObject(String objectName) {
    int partitionId = nodeEngine.getPartitionService().getPartitionId(objectName);
    Container container = containers[partitionId];
    container.destroy(objectName);
  }

  public void reset() {

  }

  public void shutdown(boolean terminate) {

  }


  public static class Container {

    final Map<String,Integer> values = new HashMap<String,Integer>();
    private void init(String objectName) {
      values.put(objectName,0);
    }
    private void destroy(String objectName) {
      values.remove(objectName);
    }
  }


}
