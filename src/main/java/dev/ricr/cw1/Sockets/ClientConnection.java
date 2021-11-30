package dev.ricr.cw1.Sockets;

import javax.websocket.Session;

/**
 * Class to deal with connection details such as
 *  - username
 *  - session
 */
public class ClientConnection extends Thread {

  private final Session session;
  private final String username; // map key? maybe not needed here?

  public ClientConnection(String username, Session session) {
    this.session = session;
    this.username = username;

    this.setName(username);
    this.start();
  }

  // this might be the only method needed here... shame
  public Session getSession() {
    return session;
  }

}
