package com.fys.ratis.example.arithmetic;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.fys.ratis.example.arithmetic.cli.Assign;
import com.fys.ratis.example.arithmetic.cli.Get;
import com.fys.ratis.example.arithmetic.cli.Server;
import com.fys.ratis.example.arithmetic.cli.SubCommandBase;
import org.apache.log4j.Level;
import org.apache.ratis.client.RaftClient;
import org.apache.ratis.server.impl.RaftServerImpl;
import org.apache.ratis.util.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Runner {

  private static List<SubCommandBase> commands = new ArrayList<>();

  static {
    LogUtils.setLogLevel(RaftServerImpl.LOG, Level.DEBUG);
    LogUtils.setLogLevel(RaftClient.LOG, Level.DEBUG);
  }

  public static void main(String[] args) throws Exception {
    initializeCommands();
    Runner runner = new Runner();
    Server server = new Server();

    JCommander.Builder builder = JCommander.newBuilder().addObject(runner);
    commands.forEach(
        command -> builder.addCommand(
            command.getClass().getSimpleName().toLowerCase(),
            command)
    );
    JCommander jc = builder.build();
    try {
      jc.parse(args);
      Optional<SubCommandBase> selectedCommand = commands.stream().filter(
                    command -> command.getClass().getSimpleName().toLowerCase()
                                .equals(jc.getParsedCommand()))
             .findFirst();
      if (selectedCommand.isPresent()) {
        selectedCommand.get().run();
      } else {
        jc.usage();
      }
    } catch (ParameterException exception) {
      System.err.println("Wrong parameters: " + exception.getMessage());
      jc.usage();
    }

  }


  private static void initializeCommands() {
    commands.add(new Server());
    commands.add(new Assign());
    commands.add(new Get());
  }
}
