package dev.ricr.cw1.Endpoints;

import dev.ricr.cw1.Database.DatabaseHandler;
import dev.ricr.cw1.Middleware.Authentication;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;

@WebServlet(name = "AvatarRoute", value = "/send/avatar")
@MultipartConfig(
    maxFileSize = 1024 * 1024 * 5 // 5mb
)
public class AvatarRoute extends HttpServlet {

  PrintWriter out;
  String[] validAvatarTypes = {"jpeg", "png", "jpg"};

  @Override
  protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

  }

  // TODO: Refactor this here
  @Override
  protected void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    out = response.getWriter();

    String filePath = request.getServletContext().getContextPath(); // path to store in the db
    String appPath = request.getServletContext().getRealPath("");
    String savePath = appPath + File.separator + "/avatars";

    // check for avatars folder and if it doesn't exist then create.
    File avatarsFolder = new File(savePath);
    if (!avatarsFolder.exists()) {
      avatarsFolder.mkdir();
    }

    // validate bearer token
    String authResponse = Authentication.authenticate(request);

    if (authResponse.contains("error")) {
      out.print(authResponse);
      return;
    }

    InputStream fileIn;
    FileOutputStream fileOut;
    String fileName = "";

    for (Part part : request.getParts()) {
      String extension = getFileExtension(part);

      if (extension == null || !Arrays.asList(validAvatarTypes).contains(extension)) {
        out.print("{\"error\": \"Invalid file uploaded.\"}");
        return;
      }

      // TODO: refactor this bit of code
      fileIn = request.getPart(part.getName()).getInputStream();
      int size = fileIn.available();
      byte[] file = new byte[size];
      fileIn.read(file);
      fileName = authResponse + "." + extension;
      fileOut = new FileOutputStream(savePath + File.separator + fileName);
      fileOut.write(file);
      fileIn.close();
      fileOut.close();
    }

    databaseStore(fileName, authResponse);
  }

  /**
   * Find avatar extension
   */
  private String getFileExtension(Part part) {
    String[] parts = part.toString().split(",");
    for (String p : parts) {
      if (p.contains("File name")) {
        return p.split("=")[1].split("\\.")[1];
      }
    }
    return null;
  }

  /**
   * DB store helper
   */
  private void databaseStore(String fileName, String username) {
    try {
      PreparedStatement statement = DatabaseHandler.doConnect()
          .prepareStatement("update users set avatar = ? where username = ?");
      statement.setString(1, fileName);
      statement.setString(2, username);
      statement.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }


}
