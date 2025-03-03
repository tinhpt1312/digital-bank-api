package org.tinhpt.digital.config.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.tinhpt.digital.share.ITokenPayload;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider tokenProvider;
    private final Set<String> skipUrls = Set.of(
            "/api/auth/login",
            "/api/auth/register",
            "/oauth2",
            "/login",
            "/api/auth/google/login-success",
            "/api/auth/google/login-failure"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return skipUrls.stream().anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = extractToken(request);
            if (token != null && tokenProvider.validateToken(token)) {
                ITokenPayload payload = tokenProvider.getPayloadFromToken(token);

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    List<SimpleGrantedAuthority> authorities = convertPermissionsToAuthorities(payload);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(payload, null, authorities);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }

        if (isOAuth2Request(request)) {
            Collections.list(request.getHeaderNames())
                    .forEach(headerName -> log.debug("{}: {}", headerName, request.getHeader(headerName)));
        }

        filterChain.doFilter(request, response);
    }

    private boolean isOAuth2Request(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.contains("oauth2") || uri.contains("login/oauth2");
    }

    private String extractToken(HttpServletRequest request) {
        String tokenFromCookie = extractTokenFromCookie(request);
        if (tokenFromCookie != null) {
            return tokenFromCookie;
        }

        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            return Arrays.stream(cookies)
                    .filter(cookie -> "jwt".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    private List<SimpleGrantedAuthority> convertPermissionsToAuthorities(ITokenPayload payload) {
        return payload.getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(
                        permission.getName() + "_" + permission.getAction()))
                .collect(Collectors.toList());
    }
}
