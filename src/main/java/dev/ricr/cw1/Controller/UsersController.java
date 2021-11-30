package dev.ricr.cw1.Controller;

import dev.ricr.cw1.Database.DatabaseHandler;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class will get the logged user details when authenticating a token and when doing a login.
 */
public class UsersController {

  /**
   * Fetch user details from the database using the username.
   *
   * @return JsonObject
   */
  public static JsonObject getUserDetails (String username) {
//    JsonObject json;
    JsonObjectBuilder jb = Json.createObjectBuilder();

    try {
      PreparedStatement statement = DatabaseHandler.doConnect()
          .prepareStatement("select * from users where username = ?");
      statement.setString(1, username);
      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {
        jb.add("email", resultSet.getString("email"))
            .add("username", username)
            .add("createdAt", resultSet.getString("created_at"))
            .add("lastLogin", resultSet.getString("last_login"));
      }

//      json = jb.build();
      return jb.build();

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
  }

}
