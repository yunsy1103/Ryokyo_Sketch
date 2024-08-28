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

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final MemberService memberService;
    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        try {

            if (path.startsWith("/api/gpt")) {
                handleGptRequest(request, response, filterChain);
                return;
            }

            if (path.equals("/api/login") || path.equals("/api/register")) {
                filterChain.doFilter(request, response);
                return;
            }

            final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

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
        String validApiKey = "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";
        return apiKey.startsWith(validApiKey);
    }
}
