package com.travel.japan.jwt;

import com.travel.japan.entity.RefreshToken;
import com.travel.japan.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private String secretKey = "c961e0b1a69f67a8da0801e32e443d949933001fc54e2a3f7263d901426d251c";
    // 토큰 유효시간 30분
    private long tokenValidTime =  30 * 60 * 1000L;
    // refreshToken 유효시간 30일
    private long refreshTokenValidTime = 30 * 24 * 60 * 60 * 1000L;

    private final UserDetailsService userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;



    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public TokenInfo generateToken(String userPk, List<String> roles){
        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put("roles", roles);
        Date now = new Date();

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime) )
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return TokenInfo.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader("Access_Token", accessToken);
    }
    public void setHeaderRefreshToken(HttpServletResponse response, String refreshToken) {
        response.setHeader("Refresh_Token", refreshToken);
    }


    public Authentication getAuthentication(String accessToken){
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(accessToken));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String getHeaderToken(HttpServletRequest request, String type) {
        return type.equals("Access") ? request.getHeader("Access_Token") : request.getHeader("Refresh_Token");
    }

    public boolean validateToken(String jwtToken) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken).getBody();
            Date expiration = claims.getExpiration();
            log.info("expiration : " + expiration);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean refreshTokenValidation(String token){

        if(!validateToken(token)) return false;

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByRefreshToken(token);

        return refreshToken.isPresent() && token.equals(refreshToken.get().getRefreshToken());

    }

}