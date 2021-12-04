package dev.ricr.cw1.Middleware;

import dev.ricr.cw1.Database.DatabaseHandler;
import dev.ricr.cw1.Utils.Http;

import javax.json.JsonObject;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO: add more validation. And also check the payload content. If any of the requested fields are empty return an error message.
 */

public class Login {

  private String responseMessage;
  private HashMap<String, String> response;

  // Pass the request here? It's a middleware after all
  private HttpServletResponse res;

  public Login doLogin (HttpServletResponse res, JsonObject json) {
    this.res = res;

    response = new HashMap<>(); // reset the hashmap on every request

    if (this.userExists(json.getString("username"), json.getString("password"))) {
      setResponseMessage("user logged in");
      response.put("message", responseMessage);
      response.put("token", Authentication.generateToken(json.getString("username"), true));

      setLastLogin(json.getString("username"));
      return this;
    }

    setResponseMessage("user details are not valid");
    response.put("message", responseMessage);

    return this;
  }

  /**
   * Check if user exists based on username && password sent
   * @param username String
   * @param password String
   * @return boolean
   */
  private boolean userExists (String username, String password) {
    try {
      PreparedStatement statement = DatabaseHandler.doConnect()
          .prepareStatement("select * from users where username = ? and password = ?");
      statement.setString(1, username);
      statement.setString(2, password);
      ResultSet resultSet = statement.executeQuery();
      if(resultSet.next()) {

        response.put("avatar", resultSet.getString("avatar"));

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
   * Set the last login timestamp
   * @param username String
   */
  private void setLastLogin(String username) {
    try {
      PreparedStatement statement = DatabaseHandler.doConnect()
          .prepareStatement("update users set last_login = now() where username = ?");
      statement.setString(1, username);
      statement.execute();

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public Map<String, String> getResponse() {
    return this.response;
  }

  private void setResponseMessage (String message) {
    this.responseMessage = message;
  }

  private String getErrorMessage () {
    return this.responseMessage;
  }
}
