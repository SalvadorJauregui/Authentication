package com.auth.security;

import com.auth.model.User;
import com.auth.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LogManager.getLogger(JwtAuthenticationFilter.class);
    private final JwtProvider jwtProvider;
    private final UserService userService;

    public JwtAuthenticationFilter(JwtProvider jwtProvider, UserService userService) {
        this.jwtProvider = jwtProvider;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (jwtProvider.validateToken(token)) {
                String email = jwtProvider.getEmailFromToken(token);
                User user = userService.findByEmail(email);

                if (user != null) {
                    List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                            .collect(Collectors.toList());

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            user.getEmail(), null, authorities);

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } else {
                LOGGER.warn("Skipping authentication: invalid token for request {}", request.getRequestURI());
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return new AntPathRequestMatcher("/api/auth/**").matches(request);
    }
}
