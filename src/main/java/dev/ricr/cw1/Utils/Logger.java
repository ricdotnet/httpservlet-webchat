package dev.ricr.cw1.Utils;

import java.io.*;
import java.util.Scanner;
import javax.json.*;

/**
 * @author ricardorocha
 */
public class Logger {

  public static void SaveLog (JsonObject json) {
    try {
      File logFile = new File("log.txt");

      if (!logFile.exists()) {
        logFile.createNewFile();
      }

      System.out.println(logFile.getAbsolutePath());

      StringBuilder data = new StringBuilder();
      Scanner readFile = new Scanner(logFile);
      while (readFile.hasNextLine()) {
        data.append(readFile.nextLine()).append("\n");
      }
      readFile.close();

      Logger.Writer(data.toString(), json);

    } catch (IOException e) {
      // ignore
      e.printStackTrace();
    }
  }

  private static void Writer (String pre, JsonObject json) {
    try {
      FileWriter file = new FileWriter("log.txt");

      StringBuilder out = new StringBuilder();
      out.append(pre).append("\n");
      out.append("{\n");
      json.keySet().forEach(el -> {
        out.append("\"").append(el).append("\": \"").append(json.getString(el)).append("\",\n");
      });
      out.append("}\n");

      file.write(out.toString());
      file.close();
    } catch (IOException e) {
      // ignore
      e.printStackTrace();
    }
  }

}
