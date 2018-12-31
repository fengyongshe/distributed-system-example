package com.fys.hazelcast.examples.container;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.spi.Operation;
import com.hazelcast.spi.PartitionAwareOperation;

import java.io.IOException;
import java.util.Map;

public class IncOperation extends Operation implements PartitionAwareOperation {

  private String objectId;
  private int amount;
  private int returnValue;

  public IncOperation() {}

  IncOperation(String objectId, int amount) {
    this.amount = amount;
    this.objectId = objectId;
  }

  @Override
  public void run() throws Exception {
    getLogger().info("Executing objectId: " + objectId + ".inc() on: " + getNodeEngine().getThisAddress());
    CounterService service = getService();
    CounterService.Container container = service.containers[getPartitionId()];
    Map<String,Integer> valuesMap = container.values;
    Integer counter = valuesMap.get(objectId);
    counter += amount;
    valuesMap.put(objectId,counter);
    returnValue = counter;
  }

  @Override
  public Object getResponse() {
    return returnValue;
  }

  protected void writeInterval(ObjectDataOutput out) throws IOException {
    super.writeInternal(out);
    out.writeUTF(objectId);
    out.writeInt(amount);
  }

  protected void readInternal(ObjectDataInput in) throws IOException {
    super.readInternal(in);
    objectId = in.readUTF();
    amount = in.readInt();
  }


}

