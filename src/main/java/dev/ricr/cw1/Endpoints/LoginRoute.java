package dev.ricr.cw1.Endpoints;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Map;
import java.util.stream.Collectors;
import javax.json.*;

import dev.ricr.cw1.Middleware.Login;
import dev.ricr.cw1.Utils.Http;

@WebServlet(name = "auth/login", value = "/auth/login")
public class LoginRoute extends HttpServlet {

  PrintWriter writer;
  JsonObject json;
  JsonReader data;
  Login login = new Login();

  @Override
  protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

  }

  @Override
  protected void doPost (HttpServletRequest request, HttpServletResponse response) throws IOException {

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

      Map<String, String> res = login.doLogin(response, json).getResponse();
      JsonObjectBuilder jsonResponseBuilder = Json.createObjectBuilder();
      res.forEach(jsonResponseBuilder::add);
      json = jsonResponseBuilder.build();

      Http.sendResponse(writer, null, null, json);

    } catch (NullPointerException | JsonException e) {
      if(e.toString().contains("NullPointerException")) {
        Http.sendResponse(writer, 401, "no body");
        return;
      }
      if(e.toString().contains("JsonException")) {
        Http.sendResponse(writer, 401, "invalid body values");
      }
    }
  }
}
