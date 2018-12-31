package com.fys.ratis.example.arithmetic;

import java.util.Map;

public interface Evaluable {

  Double evaluate(Map<String, Double> variableMap);

}
