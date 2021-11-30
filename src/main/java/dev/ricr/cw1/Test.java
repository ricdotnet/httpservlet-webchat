package dev.ricr.cw1;

import dev.ricr.cw1.Utils.Utils;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "/inc")
class Inc extends HttpServlet {

  JsonObject json;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");

    json = Json.createObjectBuilder().add("count", 1).build();

    response.getWriter().println(json);
  }
}

@WebServlet(value = "/dec")
class Dec extends HttpServlet {

  JsonObject json;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");

    json = Json.createObjectBuilder()
        .add("count", 1)
        .add("name", "chris")
        .add("email", "me@rrocha.uk")
        .add("school", "london met")
        .build();

    response.getWriter().println(json);
  }
}
