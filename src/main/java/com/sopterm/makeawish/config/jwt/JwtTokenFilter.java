package com.sopterm.makeawish.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String uri = request.getRequestURI();
            if (uri.startsWith("/favicon.ico") || uri.startsWith("/api/v1/auth") || uri.equals("/api/v1/cakes") || uri.startsWith("/api/v1/cakes/pay") || uri.startsWith("/v3/api-docs") ||
                    uri.startsWith("/swagger-ui") || (uri.equals("/api/v1/wishes/{wishId}") && request.getMethod().equals("GET")) || uri.startsWith("/health") || uri.startsWith("/cakes/approve")) {
                filterChain.doFilter(request, response);
                return;
            }
            String accessToken = getJwtFromRequest(request);
            JwtValidationType jwtValidationType = jwtTokenProvider.validateToken(accessToken);
            if (StringUtils.hasText(accessToken) && jwtValidationType == JwtValidationType.VALID_JWT) {
                Long userId = jwtTokenProvider.getUserFromJwt(accessToken);

                UserAuthentication authentication = new UserAuthentication(userId, null, null);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception exception) {
            log.error("error : ", exception);
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length());
        }
        return null;
    }
}
