package com.fys.hazelcast.examples.container;

import com.hazelcast.core.DistributedObject;

public interface Counter extends DistributedObject {

  int inc(int amount);

}
