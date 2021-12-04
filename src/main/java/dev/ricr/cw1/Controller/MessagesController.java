package dev.ricr.cw1.Controller;

import dev.ricr.cw1.Database.DatabaseHandler;

import javax.json.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MessagesController {

  JsonObject finalObject;
  JsonArrayBuilder messagesArray = Json.createArrayBuilder();
  JsonObjectBuilder messagesObject = Json.createObjectBuilder();

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
   * <p>
   * This method will run 2 queries. It will get messages sent between sender and receiver (both ways)
   * it will then generate json objects of all these messages, add them to an array and send to the client.
   */
  public JsonObject fetchMessages (String sender, String receiver) {

    messagesHelper(sender, receiver);
    messagesHelper(receiver, sender);
    messagesObject.add("messages", messagesArray);
    finalObject = messagesObject.build();

    return finalObject;
  }

  /**
   * Fetch messages helper
   */
  private void messagesHelper (String sender, String receiver) {
    try {
      PreparedStatement statement = DatabaseHandler.doConnect()
          .prepareStatement("select * from messages where sender = ? and receiver = ? order by id desc limit 50");
      statement.setString(1, sender);
      statement.setString(2, receiver);
      statement.execute();

      ResultSet resultSetMine = statement.getResultSet();

      while (resultSetMine.next()) {
        messagesArray.add(Json.createObjectBuilder()
            .add("id", resultSetMine.getInt("id"))
            .add("sender", resultSetMine.getString("sender"))
            .add("receiver", resultSetMine.getString("receiver"))
            .add("message", resultSetMine.getString("message")));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
