package dev.ricr.cw1.Controller;

import dev.ricr.cw1.Database.DatabaseHandler;

import javax.json.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MessagesController {

  JsonObject msg;

  /**
   * Save the messages into the database
   *
   * @param msg JsonObject
   */
  public void saveMessage (JsonObject msg) {
    try {
      PreparedStatement statement = DatabaseHandler.doConnect()
          .prepareStatement("insert into messages (sender, receiver, message) values (?, ?, ?)");
      statement.setString(1, msg.getString("sender"));
      statement.setString(2, msg.getString("receiver"));
      statement.setString(3, msg.getString("message"));
      statement.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Fetch some user messages. Use sender username to fetch
   * Generate a json object with all the messages and reply... (maybe last 50? or then it will be a massive object)
   */
  public JsonObject fetchMessages (String sender, String receiver) {
    try {
      PreparedStatement getMine = DatabaseHandler.doConnect()
          .prepareStatement("select * from messages where sender = ? and receiver = ?");
      getMine.setString(1, sender);
      getMine.setString(2, receiver);
      getMine.execute();

      ResultSet resultSetMine = getMine.getResultSet();

      JsonArrayBuilder jsonArray = Json.createArrayBuilder();
      JsonObjectBuilder json = Json.createObjectBuilder();
      while (resultSetMine.next()) {
        jsonArray.add(Json.createObjectBuilder()
            .add("id", resultSetMine.getInt("id"))
            .add("sender", resultSetMine.getString("sender"))
            .add("receiver", resultSetMine.getString("receiver"))
            .add("message", resultSetMine.getString("message")));
      }
//      json.add("messages", jsonArray);

      PreparedStatement getTheirs = DatabaseHandler.doConnect()
          .prepareStatement("select * from messages where sender = ? and receiver = ?");
      getTheirs.setString(1, receiver);
      getTheirs.setString(2, sender);
      getTheirs.execute();

      ResultSet resultSetTheirs = getTheirs.getResultSet();

      while (resultSetTheirs.next()) {
        jsonArray.add(Json.createObjectBuilder()
            .add("id", resultSetTheirs.getInt("id"))
            .add("sender", resultSetTheirs.getString("sender"))
            .add("receiver", resultSetTheirs.getString("receiver"))
            .add("message", resultSetTheirs.getString("message")));
      }
      json.add("messages", jsonArray);

      msg = json.build();

      return msg;
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
  }

}
