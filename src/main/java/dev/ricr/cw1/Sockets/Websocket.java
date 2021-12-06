package dev.ricr.cw1.Sockets;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;

@ServerEndpoint("/chat/{username}")
public class Websocket {

  JsonObject json;
  ClientConnection clientConnection;
  ClientHandler clientHandler = new ClientHandler();

  Threaded client;

  /**
   * Announce a new connection to the websocket, but not really necessary for here...
   * @param session Session
   */
  @OnOpen
  public void onOpen (Session session, @PathParam("username") String username) {
    clientConnection = new ClientConnection(username, session);
    clientHandler.addConnection(username, clientConnection);

//    @Deprecated
//    client
//        .setThreadName(username)
//        .startThread();

  }

  @OnMessage
  public void onMessage (String string, Session session) throws JsonException, IOException, EncodeException {
    json = Json.createReader(new StringReader(string)).readObject();

    // when a new client connects tell everyone a new client is connected and show the online icon
    if (json.getString("type").equals("connect")) {
      clientHandler.broadcast();
      return;
    }

    if (json.getString("type").equals("login")) {
      clientHandler.broadcast(json);
      return;
    }

    try {
      clientHandler.sendMessage(json);
    } catch (InvocationTargetException e) {
      System.out.println("receiver not online yet");
    }
  }

  @OnClose
  public void onClose (Session session, @PathParam("username") String username) {

    clientHandler.removeConnection(username);
    clientHandler.broadcast();

//    users.removeIf(user -> user.getSession() == session);

//    users.forEach(user -> {
//      try {
//        user.getSession().getBasicRemote().sendObject(json);
//      } catch (IOException | EncodeException e) {
//        e.printStackTrace();
//      }
//    });
  }

  @OnError
  public void onError(Session session, Throwable throwable, @PathParam("username") String username) {
    try {
      System.out.println(session + "\n has left the chat! " + username);
    } catch (IllegalStateException e) {
      System.out.println("some error");
    }
  }
}

