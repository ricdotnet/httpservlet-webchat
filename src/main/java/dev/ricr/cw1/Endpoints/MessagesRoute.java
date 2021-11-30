package dev.ricr.cw1.Endpoints;

import dev.ricr.cw1.Middleware.Authentication;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "messages/get", value = "/messages/get")
public class MessagesRoute extends HttpServlet {

  PrintWriter out;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    String username = Authentication.decodeToken(request.getHeader("authorization").split(" ")[1]).asString();
    out = response.getWriter();

    out.print("{'some':'thing'}");
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) {

  }

}
