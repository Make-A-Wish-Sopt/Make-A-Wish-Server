package com.sopterm.makeawish.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String accessToken = getJwtFromRequest(request);
            String uri = request.getRequestURI();
            if(uri.startsWith("/api/v1/auth") ||  uri.equals("/api/v1/cakes") && request.getMethod().equals(HttpMethod.GET) || uri.startsWith("/api/v1/cakes") && request.getMethod().equals(HttpMethod.POST) || uri.startsWith("/v3/api-docs") ||
                uri.startsWith("/swagger-ui") || (uri.startsWith("/api/v1/wishes") && request.getMethod().equals("GET")) || uri.startsWith("/health")) {
                filterChain.doFilter(request, response);
                return;
            }
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
