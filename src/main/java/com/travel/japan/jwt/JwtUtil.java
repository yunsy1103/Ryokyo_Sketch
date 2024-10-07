package com.travel.japan.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {

    public static String getUserName(String token, String secretKey) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token).getBody();
            return claims.getSubject(); // "sub" 클레임에서 사용자 이름을 가져옵니다.
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isExpired(String token, String secretKey) {
        try {
            Date expiration = Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token).getBody().getExpiration();
            boolean isExpired = expiration.before(new Date());
            if (isExpired) {
                System.out.println("토큰이 만료되었습니다.");
            } else {
                System.out.println("토큰이 유효합니다.");
            }
            return isExpired;
        } catch (Exception e) {
            System.err.println("토큰 파싱 중 오류 발생: " + e.getMessage());
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
