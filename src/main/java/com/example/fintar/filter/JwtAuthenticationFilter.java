package com.example.fintar.filter;

import com.example.fintar.exception.InvalidJwtException;
import com.example.fintar.service.CustomUserDetailsService;
import com.example.fintar.service.JwtBlacklistService;
import com.example.fintar.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final JwtBlacklistService jwtBlacklistService;

    private static final List<String> WHITELIST = List.of(
            "/auth/login",
            "/auth/register"
    );

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        Claims claims;

        try {
            claims = jwtService.extractClaims(jwt);
//            username = jwtService.extractClaims(jwt).getSubject();
        } catch (JwtException e) {
            // JWT invalid → authentication failure
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT");
//            return;
            throw new InvalidJwtException("Invalid JWT");
        }

        if (jwtBlacklistService.isBlacklisted(claims.getId())) throw new AuthorizationDeniedException("Unauthorized");

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (claims.getSubject() != null && authentication == null) {
            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(claims.getSubject());

            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext()
                        .setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return WHITELIST.stream().anyMatch(path::startsWith);
    }
}
