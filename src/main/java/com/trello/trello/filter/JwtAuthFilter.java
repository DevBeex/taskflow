package com.trello.trello.filter;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.trello.trello.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtils;

    public JwtAuthFilter(JwtUtil jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException, java.io.IOException {
        String jwt = parseJwt(request);

        String path = request.getServletPath();
        // Ignorar rutas públicas
        if (path.startsWith("/auth") || path.startsWith("/error")) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUsernameFromJwtToken(jwt);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, null);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Error processing JWT: {}");
        }
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            logger.error("Error in filter chain: {}");
            throw e;
        }
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
