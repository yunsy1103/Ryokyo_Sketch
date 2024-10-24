package com.travel.japan.jwt;

import com.travel.japan.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

//JWT 검증 후 인증 정보를 설정
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final MemberService memberService;
    private final String secretKey;

    @Value("${gpt.api.key}")
    private String apiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        try {
            // Swagger UI 관련 요청에 대해 필터를 적용하지 않음
            if (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") || path.startsWith("/swagger-resources")) {
                filterChain.doFilter(request, response);
                return;
            }

            if (path.startsWith("/api/gpt"))  {
                filterChain.doFilter(request, response);
                return;
            }


            if (path.startsWith("/firebase")) {
                filterChain.doFilter(request, response);
                return;
            }

            if (path.equals("/api/login") || path.equals("/api/register")) {
                filterChain.doFilter(request, response);
                return;
            }

            final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

            // 로그로 Authorization 헤더 출력
            logger.debug("Authorization 헤더: " + authorization);

            if (authorization == null || !authorization.startsWith("Bearer ")) {
                logger.error("Authorization 헤더가 없거나 잘못된 형식입니다.");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }

            String token = authorization.substring(7);
            logger.info("Token: " + token);


            if (JwtUtil.isExpired(token, secretKey)) {
                logger.error("토큰이 만료되었습니다.");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
                return;
            }

            String userName = JwtUtil.getUserName(token, secretKey);
            logger.info("Username: " + userName);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userName, null, List.of(new SimpleGrantedAuthority("USER")));
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            // 필터 체인 진행
            filterChain.doFilter(request, response);


        } catch (Exception e) {
            logger.error("Unhandled exception in JwtFilter: " + e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    //GPT API Key 인증 처리
    private void handleGptRequest(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String apiKey = authorizationHeader.substring(7);
            if (isValidApiKey(apiKey)) {
                logger.info("Valid API Key. Proceeding with the request.");

                // 임시 인증 정보 설정
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken("apiKeyUser", null, List.of(new SimpleGrantedAuthority("USER")));
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                // 필터 체인 계속 진행
                filterChain.doFilter(request, response);

            } else {
                logger.error("Invalid API Key.");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid API Key");
            }
        } else {
            logger.error("Authorization 헤더가 없거나 잘못된 형식입니다.");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Authorization format");
        }
    }

    private boolean isValidApiKey(String apiKey) {
        logger.debug("Checking API Key: " + apiKey);
        return apiKey.startsWith(apiKey);
    }
}
