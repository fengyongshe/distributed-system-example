package com.fys.ratis.example.arithmetic.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.fys.ratis.example.arithmetic.expression.DoubleValue;
import com.fys.ratis.example.arithmetic.expression.Expression;
import com.fys.ratis.example.arithmetic.expression.Variable;
import org.apache.ratis.client.RaftClient;
import org.apache.ratis.protocol.RaftClientReply;

import java.io.IOException;

@Parameters(commandDescription = "Assign value to a variable")
public class Get extends Client {

  @Parameter(names = {"--name"}, description = "Name of the variable to set", required = true)
  String name;

  @Override
  protected void operation(RaftClient client) throws IOException {
    RaftClientReply getValue =
        client.sendReadOnly(Expression.Utils.toMessage(new Variable(name)));
    Expression response =
        Expression.Utils.bytes2Expression(getValue.getMessage().getContent().toByteArray(), 0);
    System.out.println(String.format("%s=%s",name, (DoubleValue) response).toString());
  }

}
