package dev.ricr.cw1.Endpoints;

import dev.ricr.cw1.Controller.UsersController;
import dev.ricr.cw1.Middleware.Authentication;
import dev.ricr.cw1.Utils.Http;
import dev.ricr.cw1.Utils.Logger;

import javax.json.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.stream.Collectors;

@WebServlet(name = "auth/token", value = "/auth/token")
public class AuthTokenRoute extends HttpServlet {

  PrintWriter writer;
  JsonObject json;
  JsonReader data;

  @Override
  protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

  }

  @Override
  protected void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    writer = response.getWriter();

    try {
      String contentType = request.getContentType();

      if (!contentType.equals("application/json")) {
        Http.sendResponse(writer, 401, "invalid content-type");
        return;
      }

      String body = request.getReader().lines().collect(Collectors.joining());
      data = Json.createReader(new StringReader(body));
      json = data.readObject();

      // log the body
      Logger.SaveLog(json);

      String username;
      try {
        username = Authentication.decodeToken(json.getString("token")).asString();
      } catch (NullPointerException t) {
        // invalid or expired token ends the request here...
        Http.sendResponse(writer, 401, "invalid token provided");
        return;
      }

      Http.sendResponse(writer, null, null, UsersController.getUserDetails(username));

    } catch (NullPointerException | JsonException e) {
      if (e.toString().contains("NullPointerException")) {
        Http.sendResponse(writer, 401, "no body");
        return;
      }
      if (e.toString().contains("JsonException")) {
        Http.sendResponse(writer, 401, "invalid body values");
      }
    }
  }
}
