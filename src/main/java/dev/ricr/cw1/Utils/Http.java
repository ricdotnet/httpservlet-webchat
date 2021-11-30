package dev.ricr.cw1.Utils;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.PrintWriter;

public class Http {

  /**
   * Object is an optional parameter... It "prints" an array of objects that I can then select using indexes.
   *  Using Object allows me to pass different Types.
   *  Only bad of this is that when we want to pass only a json object we must pass code and message as null.
   *
   * @param writer PrintWriter
   * @param object Object[
   *               0 - code,
   *               1 - message,
   *               2 - jsonObject
   *               ]
   */
  public static void sendResponse(PrintWriter writer, Object... object) {
    if(object.length <= 2) {
      JsonObject body = Json.createObjectBuilder()
          .add("code", object[0].toString())
          .add("message", object[1].toString())
          .build();
      writer.print(body);
      return;
    }

    writer.print(object[2]);
  }

}
