package org.tinhpt.digital.config.auth;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.tinhpt.digital.config.JwtConfig;
import org.tinhpt.digital.dto.PermissionDto;
import org.tinhpt.digital.share.ITokenPayload;
import org.tinhpt.digital.share.TokenPayload;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtConfig jwtConfig;

    public String generateToken(ITokenPayload payload) {
        Date now = new Date();
        Date expiryDate = calculateExpiryDate(now);

        return buildToken(payload, now, expiryDate);
    }

    private Date calculateExpiryDate(Date now) {
        return new Date(now.getTime() + jwtConfig.getExpiration());
    }

    private String buildToken(ITokenPayload payload, Date now, Date expiryDate) {
        return Jwts.builder()
                .setSubject(payload.getUserName())
                .claim("userId", payload.getUserId())
                .claim("permissions", payload.getPermissions())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    private Set<PermissionDto> extractPermissions(Claims claims) {
        @SuppressWarnings("unchecked")
        List<Map<String, String>> permissions = (List<Map<String, String>>) claims.get("permissions", List.class);
        return permissions.stream()
                .map(this::convertToPermissionDto)
                .collect(Collectors.toSet());
    }

    private PermissionDto convertToPermissionDto(Map<String, String> permission) {
        return PermissionDto.builder()
                .name(permission.get("name"))
                .action(permission.get("action"))
                .build();
    }

    public ITokenPayload getPayloadFromToken(String token) {
        Claims claims = parseToken(token);
        Set<PermissionDto> permissions = extractPermissions(claims);
        return buildTokenPayload(claims, permissions);
    }

    private Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private TokenPayload buildTokenPayload(Claims claims, Set<PermissionDto> permissions) {
        return TokenPayload.builder()
                .userId(claims.get("userId", Long.class))
                .username(claims.getSubject())
                .permissions(permissions)
                .build();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtConfig.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
