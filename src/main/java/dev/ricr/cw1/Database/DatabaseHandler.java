package dev.ricr.cw1.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHandler {

  private static final String url = "jdbc:mysql://root@localhost:3306/javachat?useSSL=false";

  public static Connection doConnect() {

    try {
      Class.forName("com.mysql.cj.jdbc.Driver");

      //try the connection using the details set above
      return DriverManager.getConnection(url);
    } catch (SQLException e) { //error if something goes wrong
      System.out.println("SQLException: " + e.getMessage());
      System.out.println("SQLState: " + e.getSQLState());
      System.out.println("VendorError: " + e.getErrorCode());
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

    return null;
  }

}
