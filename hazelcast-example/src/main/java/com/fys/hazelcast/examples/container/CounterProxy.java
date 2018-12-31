package com.fys.hazelcast.examples.container;

import com.hazelcast.spi.AbstractDistributedObject;
import com.hazelcast.spi.InvocationBuilder;
import com.hazelcast.spi.NodeEngine;
import com.hazelcast.util.ExceptionUtil;

import java.util.concurrent.Future;

public class CounterProxy extends AbstractDistributedObject<CounterService> implements Counter  {

  private final String name;

  CounterProxy(String name, NodeEngine nodeEngine, CounterService counterService) {
    super(nodeEngine, counterService);
    this.name = name;
  }

  public int inc(int amount) {
    NodeEngine nodeEngine = getNodeEngine();
    IncOperation operation = new IncOperation(name, amount);
    int partitionId = nodeEngine.getPartitionService().getPartitionId(name);
    InvocationBuilder builder = nodeEngine.getOperationService()
        .createInvocationBuilder(CounterService.NAME, operation, partitionId);
    try {
      Future<Integer> future = builder.invoke();
      return future.get();
    } catch (Exception e) {
      throw ExceptionUtil.rethrow(e);
    }
  }

  public String getName() {
    return name;
  }

  public String getServiceName() {
    return CounterService.NAME;
  }

}
