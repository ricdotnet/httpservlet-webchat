package dev.ricr.cw1.Middleware;

import dev.ricr.cw1.Database.DatabaseHandler;
import dev.ricr.cw1.Utils.Http;

import javax.json.JsonObject;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * TODO: add more validation. If user is new then check for their username size, email type and password length?
 * This can also be done in the front end but because endpoints can be found easily then I need extra checks in the backend too.
 */

public class Register {

  private String responseMessage;
  private HttpServletResponse res;

  /**
   * Verify Username
   *
   * @param username String
   * @return boolean
   */
  private boolean isUsernameValid (String username) {
    return exists(username, "username");
  }

  /**
   * Verify Password
   *
   * @param password String
   * @return boolean
   */
  private boolean isPasswordValid (String password) {
    return false;
  }

  /**
   * Verify Email Address
   *
   * @param email String
   * @return boolean
   */
  private boolean isEmailValid (String email) {
    return exists(email, "email");
  }

  /**
   *
   * @param json JsonObject
   * @return this class
   */
  public Register doRegister (HttpServletResponse res, JsonObject json) {
    this.res = res;

    if (this.isUsernameValid(json.getString("username"))) {
      this.setResponse("username already exists");
      return this;
    }

    if(this.isEmailValid(json.getString("email"))) {
      this.setResponse("email already exists");
      return this;
    }

    try {
      PreparedStatement statement = DatabaseHandler.doConnect()
          .prepareStatement("insert into users (username, password, email) values (?, ?, ?)");

      statement.setString(1, json.getString("username"));
      statement.setString(2, json.getString("password"));
      statement.setString(3, json.getString("email"));
      statement.execute();

      this.setResponse("account registered");
    } catch (SQLException e) {
      try {
        Http.sendResponse(res.getWriter(), 400, "something went wrong");
      } catch (IOException ignore) {} // IGNORED!
    }

    return this;
  }

  /**
   * Check if the given value exists in the table already, this is to check for duplicate data...
   * @param value String
   * @return boolean
   */
  private boolean exists(String value, String col) {
    try {
      PreparedStatement statement = DatabaseHandler.doConnect().prepareStatement("select * from users where " + col + " = ?");
      statement.setString(1, value);
      statement.executeQuery();

      ResultSet resultSet = statement.getResultSet();

      if(resultSet.next()) {
        return true;
      }

    } catch (SQLException e) {
      try {
        Http.sendResponse(res.getWriter(), 400, "something went wrong");
      } catch (IOException ignore) {} // IGNORED!
    }
    return false;
  }

  /**
   * Set the error message
   * @param message String
   */
  private void setResponse(String message) {
    this.responseMessage = message;
  }

  /**
   * Return the error message (doesn't need to really error, it can be a successful message too)
   * @return String
   */
  public String getResponse() {
    return this.responseMessage;
  }

}
