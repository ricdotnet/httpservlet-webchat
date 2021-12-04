package dev.ricr.cw1.Endpoints;

import dev.ricr.cw1.Controller.MessagesController;
import dev.ricr.cw1.Middleware.Authentication;

import javax.json.JsonObject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "messages/get/username", value = "/messages/get/*")
public class MessagesRoute extends HttpServlet {

  PrintWriter out;
  MessagesController messagesController = new MessagesController();

  /**
   * When selecting someone to chat the client will run a request to this endpoint.
   * It will send a Bearer token and that token will contain the username of the logged user.
   * This will then fetch the messages related to the sender (logged user) and the receiver (user clicked on)
   * A json object will be created and sent back to the client.
   *
   * @param request HttpServletRequest
   * @param response HttpServletResponse
   * @throws IOException IOException i guess
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    out = response.getWriter();
    String authHeader = request.getHeader("authorization");
    String username = "";
    String receiver = "";

    if (authHeader == null) {
      out.print("{\"error\": \"No authorization header present.\"}");
      return;
    }

    try {
      username = Authentication.decodeToken(authHeader.split(" ")[1]).asString();
    } catch (ArrayIndexOutOfBoundsException e) {
      out.print("{\"error\": \"No token provided.\"}");
      return;
    } catch (NullPointerException e) {
      out.print("{\"error\": \"Invalid token provided.\"}");
      return;
    }

    if (username == null) {
      out.print("{\"error\": \"Invalid token provided.\"}");
      return;
    }

    try {
      receiver = request.getPathInfo().split("/")[1];
    } catch (ArrayIndexOutOfBoundsException e) {
      out.print("{\"error\": \"Invalid request url.\"}");
      return;
    }

    if (receiver == null) {
      out.print("{\"error\": \"Invalid request url.\"}");
      return;
    }

    JsonObject res = messagesController.fetchMessages(username, receiver);
    out.print(res);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) {

  }

}
