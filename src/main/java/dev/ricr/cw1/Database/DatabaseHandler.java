package dev.ricr.cw1.Database;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHandler {

//  private static final String url = "jdbc:mysql://root@localhost:3306/javachat?useSSL=false";

  public static Connection doConnect() {

    try {
//      Class.forName("com.mysql.cj.jdbc.Driver");
      InitialContext ctx = new InitialContext();
      DataSource ds = (DataSource) ctx.lookup("jdbc/MySQLDataSource");

      //try the connection using the details set above
//      return DriverManager.getConnection(url);
      return ds.getConnection();
    } catch (SQLException e) { //error if something goes wrong
      System.out.println("SQLException: " + e.getMessage());
      System.out.println("SQLState: " + e.getSQLState());
      System.out.println("VendorError: " + e.getErrorCode());
    } catch (NamingException e) {
      e.printStackTrace();
    }
//    catch (ClassNotFoundException e) {
//      e.printStackTrace();
//    }

    return null;
  }

}
