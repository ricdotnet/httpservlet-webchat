package dev.ricr.cw1.Middleware;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Date;

public class Authentication {

  static Algorithm algorithm = Algorithm.HMAC512("yeet");
  static Calendar c = Calendar.getInstance();

  /**
   * Generate and print a jwt token
   * @return String
   */
  public static String generateToken(String username, boolean rememberMe) {
    c.setTime(new Date());
    if (rememberMe) {
      c.add(Calendar.DATE, 15);
//      c.add(Calendar.MINUTE, 4);
    }
    return JWT.create()
        .withClaim("username", username)
        .withExpiresAt(c.getTime())
        .sign(algorithm);
  }

  /**
   * Decode the jwt token and get its data
   * @return Claim
   */
  public static Claim decodeToken(String token) {
    try {
      DecodedJWT decode = JWT.decode(token);

      if (isExpired(decode.getClaim("exp").asLong())) {
        return null;
      }

      return decode.getClaim("username");
    } catch (JWTDecodeException e) {
      return null;
    }
  }

  /**
   * Check the token expiry date
   */
  private static boolean isExpired(long exp) {
    return exp < (new Date().getTime()/1000);
  }

}
