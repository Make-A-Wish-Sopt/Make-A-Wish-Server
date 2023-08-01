package com.sopterm.makeawish.jwt;

import com.sopterm.makeawish.domain.user.InternalTokenManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

import static org.springframework.util.StringUtils.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final InternalTokenManager tokenManager;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            val jwtToken = parseJwt(request);
            if (checkJwtAvailable(jwtToken)) {
                val auth = tokenManager.getAuthentication(jwtToken);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception exception) {
            log.error("error: ", exception);
        }

        filterChain.doFilter(request, response);
    }

    private boolean checkJwtAvailable (String jwtToken) {
        return hasText(jwtToken) && tokenManager.verifyAuthToken(jwtToken);
    }

    private String parseJwt(HttpServletRequest request) {
        val headerAuth = request.getHeader("Authorization");
        return hasText(headerAuth) && headerAuth.startsWith("Bearer ")
            ? headerAuth.substring("Bearer ".length())
            : null;
    }
}
