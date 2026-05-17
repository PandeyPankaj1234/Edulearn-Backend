package com.edulearn.auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            // ── Dev mock-token bypass ─────────────────────────────────────────
            // Tokens in format "mock-<Role>" (e.g. mock-Admin, mock-Student)
            // allow UI testing without a real JWT. Never enable in production.
            if (token.startsWith("mock-")) {
                String role = token.substring(5); // e.g. "Admin", "Student", "Instructor"
                if (!role.isEmpty()) {
                    UsernamePasswordAuthenticationToken mockAuth =
                            new UsernamePasswordAuthenticationToken(
                                    "mock@edulearn.dev",
                                    null,
                                    List.of(new SimpleGrantedAuthority("ROLE_" + role))
                            );
                    SecurityContextHolder.getContext().setAuthentication(mockAuth);
                }
            }
            // ── Real JWT validation ───────────────────────────────────────────
            else if (jwtUtil.validateToken(token)) {
                String email = jwtUtil.extractEmail(token);
                String role  = jwtUtil.extractRole(token);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_" + role))
                        );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
