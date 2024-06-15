package com.travel.japan.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.japan.dto.GlobalResDto;
import com.travel.japan.entity.RefreshToken;
import com.travel.japan.repository.RefreshTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String accessToken = jwtTokenProvider.getHeaderToken((HttpServletRequest) request, "Access");
        String refreshToken = jwtTokenProvider.getHeaderToken((HttpServletRequest) request, "Refresh");

        if(accessToken != null){

            //access 유효한 경우
            if (jwtTokenProvider.validateToken(accessToken)){
                Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("accessToken 유효");
            }

            else if(refreshToken != null){

                boolean isRefreshToken = jwtTokenProvider.refreshTokenValidation(refreshToken);
                // access 만료 && refresh 유효
                if(isRefreshToken){

                    Optional<RefreshToken> refreshToken1 = refreshTokenRepository.findByRefreshToken(refreshToken);

                    String email = refreshToken1.get().getEmail();
                    TokenInfo tokenInfo = jwtTokenProvider.generateToken(email, Collections.singletonList("ROLE_USER"));
                    String newAccessToken = tokenInfo.getAccessToken();

                    jwtTokenProvider.setHeaderAccessToken((HttpServletResponse) response, newAccessToken);

                    Authentication authentication = jwtTokenProvider.getAuthentication(newAccessToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("accessToken만료, refreshToken 발급함");

                }

                // access , refresh 둘다 만료
                else{
                    jwtExceptionHandler((HttpServletResponse) response, "RefreshToken Expired", HttpStatus.BAD_REQUEST);
                    log.info("accessToken,refreshToken 둘 다 만료");
                    return;
                }

            }
        }
        chain.doFilter(request, response);
    }

    public void jwtExceptionHandler(HttpServletResponse response, String msg, HttpStatus status) {
        response.setStatus(status.value());
        response.setContentType("application/json");
        try {
            String json = new ObjectMapper().writeValueAsString(new GlobalResDto(msg, status.value()));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}