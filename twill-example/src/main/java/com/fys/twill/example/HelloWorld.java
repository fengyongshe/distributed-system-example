package com.fys.twill.example;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.twill.api.AbstractTwillRunnable;
import org.apache.twill.api.ClassAcceptor;
import org.apache.twill.api.TwillController;
import org.apache.twill.api.TwillRunnerService;
import org.apache.twill.api.logging.PrinterLogHandler;
import org.apache.twill.yarn.YarnTwillRunnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class HelloWorld {

  public static final Logger LOG = LoggerFactory.getLogger(HelloWorld.class);

  public static void main(String[] args) {
    if (args.length < 1) {
      System.err.println("Arguments format: <host:port of zookeeper server>");
      System.exit(1);
    }
    String zkStr = args[0];
    YarnConfiguration yarnConfiguration = new YarnConfiguration();
    final TwillRunnerService twillRunner = new YarnTwillRunnerService(yarnConfiguration, zkStr);
    twillRunner.start();

    String yarnClasspath =
        yarnConfiguration.get(YarnConfiguration.YARN_APPLICATION_CLASSPATH,
            Joiner.on(",").join(YarnConfiguration.DEFAULT_YARN_APPLICATION_CLASSPATH));
    List<String> applicationClassPaths = Lists.newArrayList();
    Iterables.addAll(applicationClassPaths, Splitter.on(",").split(yarnClasspath));

    final TwillController controller = twillRunner.prepare(new HelloWorldRunnable())
        .addLogHandler(new PrinterLogHandler(new PrintWriter(System.out, true)))
        .withApplicationClassPaths(applicationClassPaths)
        .withBundlerClassAcceptor(new HadoopClassExcluder())
        .start();

    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        try {
          Futures.getUnchecked(controller.terminate());
        } finally {
          twillRunner.stop();
        }
      }
    });

    try {
      controller.awaitTerminated();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }

  }

  private static class HelloWorldRunnable extends AbstractTwillRunnable {
    public void run() {
      LOG.info("Hello Twill, My First distributed application.");
    }
  }

  static class HadoopClassExcluder extends ClassAcceptor {

    @Override
    public boolean accept(String className, URL classUrl, URL classPathUrl) {
      return !(className.startsWith("org.apache.hadoop") && !className.startsWith("org.apache.hadoop.hbase"));
    }
  }
}
