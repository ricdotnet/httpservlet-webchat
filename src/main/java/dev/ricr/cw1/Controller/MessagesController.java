package dev.ricr.cw1.Controller;

import dev.ricr.cw1.Database.DatabaseHandler;

import javax.json.JsonObject;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MessagesController {

  JsonObject msg;

  /**
   * Save the messages into the database
   * @param msg JsonObject
   */
  public void saveMessage(JsonObject msg) {
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
   * @param obj JsonObject
   */
  public void fetchMessages(JsonObject obj) {
    try {
      PreparedStatement statement = DatabaseHandler.doConnect()
          .prepareStatement("select * from messages where sender = ?");
      statement.setString(1, obj.getString("sender"));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
