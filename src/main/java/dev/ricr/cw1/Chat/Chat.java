package dev.ricr.cw1.Chat;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.StringReader;
import java.util.stream.Collectors;

@WebServlet(name = "api/chat", value = "/api/chat/*")
public class Chat extends HttpServlet {

  JsonObject json;

  @Override
  protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

  }

  @Override
  protected void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//    String user = request.getRequestURI();
    String contentType = request.getContentType();

    String body = request.getReader().lines().collect(Collectors.joining());
    json = Json.createReader(new StringReader(body)).readObject();


  }
}
