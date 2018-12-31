package com.fys.ignite.example.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class TestJDBC {

  public static void main(String[] args) throws Exception {
    Class.forName("org.apache.ignite.IgniteJdbcThinDriver");
    Connection conn = DriverManager.getConnection("jdbc:ignite:thin://10.139.4.83/");
    try (Statement stmt  = conn.createStatement()){
      // Create table based on REPLICATED template.
      stmt.executeUpdate("CREATE TABLE City (" +
          " id LONG PRIMARY KEY, name VARCHAR) " +
          " WITH \"template=replicated\"");

      // Create table based on PARTITIONED template with one backup.
      stmt.executeUpdate("CREATE TABLE Person (" +
          " id LONG, name VARCHAR, city_id LONG, " +
          " PRIMARY KEY (id, city_id)) " +
          " WITH \"backups=1, affinityKey=city_id\"");

      // Create an index on the City table.
      stmt.executeUpdate("CREATE INDEX idx_city_name ON City (name)");

      // Create an index on the Person table.
      stmt.executeUpdate("CREATE INDEX idx_person_name ON Person (name)");
    }
  }

}
