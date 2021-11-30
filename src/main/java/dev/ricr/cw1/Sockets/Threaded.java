package dev.ricr.cw1.Sockets;

@Deprecated
public class Threaded extends Thread {

  private String threadName;

  public Threaded() {}

  public Threaded setThreadName(String threadName) {
    this.threadName = threadName;
    this.setName(this.threadName);
    return this;
  }

  public Threaded startThread() {
    this.start();
    return this;
  }

  public String getThreadName() {
    return this.threadName;
  }

}
