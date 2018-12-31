package com.fys.ratis.example.arithmetic.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.fys.ratis.example.arithmetic.AssignmentMessage;
import com.fys.ratis.example.arithmetic.expression.BinaryExpression;
import com.fys.ratis.example.arithmetic.expression.DoubleValue;
import com.fys.ratis.example.arithmetic.expression.Expression;
import com.fys.ratis.example.arithmetic.expression.UnaryExpression;
import com.fys.ratis.example.arithmetic.expression.Variable;
import com.google.common.annotations.VisibleForTesting;
import org.apache.ratis.client.RaftClient;
import org.apache.ratis.protocol.RaftClientReply;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Parameters(commandDescription = "Assign value to a variable")
public class Assign extends Client {

  Pattern binaryOperationPattern = Pattern.compile("([a-z1-9]*)([\\*\\-/\\+])([a-z1-9]*)");
  Pattern unaryOperationPattern = Pattern.compile("([√~])([a-z1-9]+)");

  @Parameter(names = {"--name"}, description = "Name of the variable to set", required = true)
  String name;

  @Parameter(names = {"--value"}, description = "Value to set", required = true)
  String value;

  @Override
  protected void operation(RaftClient client) throws IOException {
    RaftClientReply send = client.send(
        new AssignmentMessage(new Variable(name),createExpression(value))
    );
    System.out.println("Success:" + send.isSuccess());
    System.out.println("Response:" + send.getMessage().getClass());
  }

  @VisibleForTesting
  protected Expression createExpression(String value) {
    if (value.matches("\\d*(\\.\\d*)?")) {
      return new DoubleValue(Double.valueOf(value));
    } else if (value.matches("[a-zA-Z]+")) {
      return new Variable(value);
    }
    Matcher binaryMatcher = binaryOperationPattern.matcher(value);
    Matcher unaryMatcher = unaryOperationPattern.matcher(value);

    if (binaryMatcher.matches()) {
      return createBinaryExpression(binaryMatcher);
    } else if (unaryMatcher.matches()) {
      return createUnaryExpression(unaryMatcher);
    } else {
      throw new IllegalArgumentException("Invalid expression " + value + " Try something like: 'a+b' or '2'");
    }
  }

  private Expression createBinaryExpression(Matcher binaryMatcher) {
    String operator = binaryMatcher.group(2);
    String firstElement = binaryMatcher.group(1);
    String secondElement = binaryMatcher.group(3);
    Optional<BinaryExpression.Op> selectedOp =
        Arrays.stream(BinaryExpression.Op.values()).filter(op -> op.getSymbol().equals(operator)).findAny();

    if (!selectedOp.isPresent()) {
      throw new IllegalArgumentException("Unknown binary operator: " + operator);
    } else {
      return new BinaryExpression(selectedOp.get(), createExpression(firstElement), createExpression(secondElement));
    }
  }

  private Expression createUnaryExpression(Matcher binaryMatcher) {
    String operator = binaryMatcher.group(1);
    String element = binaryMatcher.group(2);
    Optional<UnaryExpression.Op> selectedOp =
        Arrays.stream(UnaryExpression.Op.values()).filter(op -> op.getSymbol().equals(operator)).findAny();

    if (!selectedOp.isPresent()) {
      throw new IllegalArgumentException("Unknown unary operator:" + operator);
    } else {
      return new UnaryExpression(selectedOp.get(), createExpression(element));
    }
  }

}
