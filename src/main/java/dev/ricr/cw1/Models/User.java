package dev.ricr.cw1.Models;

import javax.websocket.Session;

public class User {

  private String username;
  private final String password;
  private final String email;
  private String createdAt;
  private String lastLogin;
  private Session session;

  public User(String username, Session session) {
    this.username = username;
    this.session = session;

    this.email = null;
    this.password = null;
    this.createdAt = null;
    this.lastLogin = null;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getUsername() {
    return this.username;
  }

  public Session getSession() {
    return this.session;
  }

  public String getEmail() {
    return this.email;
  }

  public String getCreatedAt() {
    return this.createdAt;
  }

  public String getLastLogin() {
    return this.lastLogin;
  }

}
