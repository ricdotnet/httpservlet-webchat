package dev.ricr.cw1.Sockets;

import dev.ricr.cw1.Controller.MessagesController;

import javax.json.*;
import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler {

  // map to save connections with a key (username or id) - value (connection, including the session) mapping
  private final static HashMap<String, ClientConnection> connections = new HashMap<>();

  MessagesController messagesController = new MessagesController();

  JsonObject json;

  public ClientHandler () {
  }

  /**
   * Add a connection in the list
   *
   * @param connection Connection
   * @param username   String
   */
  public void addConnection (String username, ClientConnection connection) {
    connections.put(username, connection);
  }

  /**
   * Remove a connection from the list
   *
   * @param username String
   */
  public void removeConnection (String username) {
    connections.remove(username);
  }

  /**
   * Get a session based on username
   *
   * @param username String
   * @return Session
   */
  public Session getSession (String username) {
    return connections.get(username).getSession();
  }

  /**
   * Send message / event to desired user
   *
   * This will send all events... messages, typing events and more... so I need a rule to check if it is a message
   *  then I want to store it in the database. I can check the type of "event"
   *
   * @param obj JsonObject
   */
  public void sendMessage (JsonObject obj) throws EncodeException, IOException, InvocationTargetException {
    try {

      // TODO: Insert the message into database
      if (obj.getString("type").equals("message"))
        messagesController.saveMessage(obj);

      ClientConnection c = connections.get(obj.getString("receiver"));
      c.getSession().getBasicRemote().sendObject(obj);
    } catch (NullPointerException e) {
      System.out.println("receiver not logged in yet");
    }
//      getConnection(obj.getString("receiver"))
//          .getBasicRemote()
//          .sendObject(obj);
  }

  /**
   * A simple broadcast method
   *
   * @param object -> optional message object to broadcast
   * The way this works is as follows:
   *    when a user logs in it will send 2 message events, login and connect.
   *     - the login event will broadcast to everyone that's currently online and show that the new user is online now.
   *     - the connect event will ask the server for a list of all currently online users.
   */
  public void broadcast (JsonObject... object) {
    // this method gets an JsonObject(optional) and if there is no object passed it will run the first event
    // TODO: Refactor and make it work for the various broadcast events
    if (object.length == 0) {
      Map<String, String> config = new HashMap<>();
      JsonBuilderFactory factory = Json.createBuilderFactory(config);

      JsonObjectBuilder userObject = Json.createObjectBuilder();
      JsonArrayBuilder users = Json.createArrayBuilder();
      connections.keySet().forEach(el -> {
        users.add(factory.createObjectBuilder()
            .add("username", el));
      });
      userObject.add("users", users.build());
      userObject.add("type", "connect");
      json = userObject.build();
    } else {
      json = object[0];
    }

    connections.keySet().forEach(el -> {
      ClientConnection c = connections.get(el);
      synchronized (c) {
        try {
          c.getSession()
              .getBasicRemote()
              .sendObject(json);
        } catch (EncodeException | IOException e) {
          e.printStackTrace();
        }
      }
    });
  }

  public int howManyUsers () {
    return connections.size();
  }

}
