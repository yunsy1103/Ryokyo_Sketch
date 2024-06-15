package com.travel.japan.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {

    //public static String getUserName(String token, String secretKey){
       // String res = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
         //       .getBody().get("userName", String.class);
        //System.out.println("res : " + res);
    //  return res;
    //}
    public static String getUserName(String token, String secretKey) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            return claims.getSubject(); // "sub" 클레임에서 사용자 이름을 가져옵니다.
        } catch (Exception e) {
            return null;
        }
    }

  //  public static boolean isExpired(String token, String secretKey){
    //    return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token)
      //          .getBody().getExpiration().before(new Date());
    //}

    public static boolean isExpired(String token, String secretKey) {
        try {
            Date expiration = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    public static String createJwt(String username, String secretKey, Long expiredMs){
        Claims claims = Jwts.claims();
        claims.put("userName", username);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

    }
}
