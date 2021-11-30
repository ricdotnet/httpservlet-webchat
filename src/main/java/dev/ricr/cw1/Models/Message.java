package dev.ricr.cw1.Models;

public class Message {

  private final String sender;
  private final String receiver;
  private final String message;
  private String sentAt;
  private String readAt;

  public Message(String sender, String receiver, String message) {
    this.sender = sender;
    this.receiver = receiver;
    this.message = message;
    this.sentAt = null;
    this.readAt = null;
  }

}
