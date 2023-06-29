package com.sopterm.makeawish.controller.filter;

import com.sopterm.makeawish.domain.user.InternalTokenManager;
import com.sopterm.makeawish.exception.WrongAccessTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.sopterm.makeawish.common.message.ErrorMessage.WRONG_TOKEN;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final InternalTokenManager tokenManager;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {
        val jwtToken = parseJwt(request);
        val isTokenAvailable = checkJwtAvailable(jwtToken);
        val uri = request.getRequestURI();
        if (uri.startsWith("/api") && (!uri.contains("presents") && !uri.contains("auth"))) {
            if (!isTokenAvailable){
                throw new WrongAccessTokenException(WRONG_TOKEN.getMessage());
            }
        }

        if (isTokenAvailable) {
            val auth = tokenManager.getAuthentication(jwtToken);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }

    private boolean checkJwtAvailable (String jwtToken) {
        return jwtToken != null && tokenManager.verifyAuthToken(jwtToken);
    }

    private String parseJwt (HttpServletRequest request) {
        val headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth)) return headerAuth;
        return null;
    }
}
