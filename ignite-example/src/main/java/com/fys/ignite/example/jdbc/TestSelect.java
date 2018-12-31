package com.fys.ignite.example.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestSelect {

  public static void main(String[] args) throws Exception {
    // Register JDBC driver
    Class.forName("org.apache.ignite.IgniteJdbcThinDriver");

// Open JDBC connection
    Connection conn = DriverManager.getConnection("jdbc:ignite:thin://10.139.4.83/");
    try (Statement stmt = conn.createStatement()) {
      try (ResultSet rs =
               stmt.executeQuery("SELECT p.name, c.name " +
                   " FROM Person p, City c " +
                   " WHERE p.city_id = c.id")) {

        while (rs.next())
          System.out.println(rs.getString(1) + ", " + rs.getString(2));
      }
    }
  }
}
