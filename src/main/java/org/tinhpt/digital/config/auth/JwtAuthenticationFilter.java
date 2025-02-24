package org.tinhpt.digital.config.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.tinhpt.digital.config.JwtConfig;
import org.tinhpt.digital.share.ITokenPayload;

import java.io.IOException;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;

    private final JwtConfig jwtConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try{
            String jwt = getJwtFromRequest(request);

            if(StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)){
                ITokenPayload payload = tokenProvider.getPayloadFromToken(jwt);

                Collection<? extends GrantedAuthority> authorities = payload.getPermissions()
                        .stream()
                        .map(p -> new SimpleGrantedAuthority(p.getName() + "_" + p.getAction()))
                        .toList();

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(payload, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch (Exception e){
            logger.error("Could not set user authentication in security context", e);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader(jwtConfig.getHeaderString());

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(jwtConfig.getTokenPrefix())){
            return bearerToken.substring(jwtConfig.getTokenPrefix().length() + 1);
        }

        return null;
    }
}
