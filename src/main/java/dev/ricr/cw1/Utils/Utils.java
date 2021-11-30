package dev.ricr.cw1.Utils;

import java.util.Date;

public class Utils {

  Long time;

  public Utils getDate() {
    return this;
  }

  public Utils today() {
    time = new Date().getTime();
    return this;
  }

  public long get() {
    return time;
  }

}
