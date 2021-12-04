package dev.ricr.cw1.Endpoints;

import dev.ricr.cw1.Database.DatabaseHandler;
import dev.ricr.cw1.Middleware.Authentication;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(name = "AvatarRoute", value = "/send/avatar")
@MultipartConfig(
    maxFileSize = 1024 * 1024 * 5 // 5mb
)
public class AvatarRoute extends HttpServlet {

  @Override
  protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

  }

  // TODO: Refactor this here
  @Override
  protected void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    String filePath = request.getServletContext().getContextPath(); // path to store in the db
    String appPath = request.getServletContext().getRealPath("");
    String savePath = appPath + File.separator + "/avatars";

    // creates the save directory if it does not exists
    File fileSaveDir = new File(savePath);
    if (!fileSaveDir.exists()) {
      fileSaveDir.mkdir();
    }

    String username = Authentication.decodeToken(request.getHeader("authorization").split(" ")[1]).asString();

    InputStream fileIn;
    FileOutputStream fileOut;

    for (Part part : request.getParts()) {
      fileIn = request.getPart(part.getName()).getInputStream();
      int size = fileIn.available();
      byte[] file = new byte[size];
      fileIn.read(file);
      String fileName = username + ".jpeg";
      fileOut = new FileOutputStream(savePath + File.separator + fileName);
      fileOut.write(file);
      fileIn.close();
      fileOut.close();
    }

    try {
      PreparedStatement statement = DatabaseHandler.doConnect()
          .prepareStatement("update users set avatar = ? where username = ?");
      statement.setString(1, username+".jpeg");
      statement.setString(2, username);
      statement.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
