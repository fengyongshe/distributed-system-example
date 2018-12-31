package com.fys.helix.cluster;
import org.apache.helix.HelixDefinedState;
import org.apache.helix.manager.zk.ZKHelixAdmin;
import org.apache.helix.model.StateModelDefinition;

public class TestResource {

  public static void main(String[] args) {
    String ZK_ADDRESS = "fys1.cmss.com:2181";
    String CLUSTER_NAME = "helix-demo";
    String STATE_MODEL_NAME = "MasterSlave";

    ZKHelixAdmin admin = new ZKHelixAdmin(ZK_ADDRESS);

    StateModelDefinition.Builder builder = new StateModelDefinition.Builder(STATE_MODEL_NAME);
    builder.addState("MASTER",1);
    builder.addState("SLAVE",2);
    builder.addState("OFFLINE",3);
    for (HelixDefinedState state : HelixDefinedState.values()) {
      builder.addState(state.name());
    }

    builder.initialState("OFFLINE");

    builder.addTransition("OFFLINE","SLAVE");
    builder.addTransition("SLAVE","OFFLINE");
    builder.addTransition("SLAVE","MASTER");
    builder.addTransition("MASTER","SLAVE");
    builder.addTransition("OFFLINE", HelixDefinedState.DROPPED.name());

    builder.upperBound("MASTER", 1);
    builder.dynamicUpperBound("SLAVE","R");

    StateModelDefinition myStateModel = builder.build();
    admin.addStateModelDef(CLUSTER_NAME, STATE_MODEL_NAME, myStateModel);
  }


}
