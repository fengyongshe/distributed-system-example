package com.fys.ratis.example.arithmetic.expression;

import org.apache.ratis.util.Preconditions;

import java.util.Map;
import java.util.Objects;

public class DoubleValue implements Expression {

  private final double value;

  public DoubleValue(double value) {
    this.value = value;
  }

  DoubleValue(byte[] buf, int offset) {
    this(Utils.bytes2double(buf, offset + 1));
    Preconditions.assertTrue(buf[offset] == Type.DOUBLE.byteValue());
  }

  @Override
  public int toBytes(byte[] buf, int offset) {
    Preconditions.assertTrue(offset + length() <= buf.length);
    buf[offset++] = Type.DOUBLE.byteValue();
    Utils.double2bytes(value, buf, offset);
    return length();
  }

  @Override
  public int length() {
    return 9;
  }

  @Override
  public Double evaluate(Map<String, Double> variableMap) {
    return value;
  }

  @Override
  public String toString() {
    final long n = (long)value;
    return n == value? String.valueOf(n): String.valueOf(value);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DoubleValue that = (DoubleValue) o;
    return Double.compare(that.value, value) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

}
