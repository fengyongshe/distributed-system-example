package com.fys.flume.example;

import org.apache.flume.Channel;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.Transaction;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;


public class MySink extends AbstractSink implements Configurable {

  private static final Logger logger = LoggerFactory.getLogger(MySink.class);
  private static final String PROP_KEY_ROOTPATH = "fileName";
  private String fileName;

  public void configure(Context context) {
    fileName = context.getString(PROP_KEY_ROOTPATH);
  }

  public Status process() throws EventDeliveryException {

    Channel ch = getChannel();
    Transaction txn = ch.getTransaction();
    txn.begin();

    Event event = null;
    while(true) {
      event = ch.take();
      if(event != null) {
        break;
      }
    }

    try {
      logger.info("Get Event");
      String body = new String(event.getBody());
      System.out.println("Event.getBody: "+ body);
      String res = body + ":" + System.currentTimeMillis() + "\r\n";
      File file = new File(fileName);
      FileOutputStream fos = new FileOutputStream(file,true);
      fos.write(res.getBytes());
      fos.close();
      txn.commit();
      return Status.READY;
    } catch (Exception ex) {
      txn.rollback();
      throw new EventDeliveryException(ex.getMessage());
    } finally {
      txn.close();
    }
  }


}
