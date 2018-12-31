package com.fys.sandbox.module;

import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.ProcessControlException;
import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.api.filter.NameRegexFilter;
import com.alibaba.jvm.sandbox.api.http.Http;
import com.alibaba.jvm.sandbox.api.listener.EventListener;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;

import javax.annotation.Resource;

@Information(id = "broken-clock-tinker")
public class BrokenClockTinkerModule implements Module {

  @Resource
  private ModuleEventWatcher moduleEventWatcher;

  @Http("/repairCheckState")
  public void repairCheckState() {
    moduleEventWatcher.watch(
        new NameRegexFilter("com\\.fys\\.sandbox\\.demo\\.BrokenClock",
            "checkState"),
        new EventListener() {
          public void onEvent(Event event) throws Throwable {
            ProcessControlException.throwReturnImmediately(null);
          }
        },
        Event.Type.THROWS
    );
  }

  @Http("/repairDelay")
  public void repairDelay() {
    moduleEventWatcher.watch(
        new NameRegexFilter("com\\.fys\\.sandbox\\.demo\\.BrokenClock",
            "delay"),
        new EventListener() {
          public void onEvent(Event event) throws Throwable {
            Thread.sleep(1000L);
            System.out.println("Call Before invoked");
            ProcessControlException.throwReturnImmediately(null);
          }
        },
        Event.Type.CALL_BEFORE
    );
  }

}
