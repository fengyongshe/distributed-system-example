package com.fys.ignite.example.service;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;

public class ServiceGridExample {

  public static void main(String[] args) throws Exception {
    try (Ignite ignite = Ignition.start()) {

      ignite.services().deployClusterSingleton("WeatherService", new WeatherServiceImpl());
      WeatherService service = ignite.services().service("WeatherService");
      String forecast = service.getCurrentTemperature("London","UK");
      System.out.println("Weather forecast in Londown:" + forecast);
    }
  }
}
