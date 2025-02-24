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

    public String generateToken(ITokenPayload payload){
        Date now = new Date();

        Date expiryDate = new Date(now.getTime() + jwtConfig.getExpiration());

        return Jwts.builder()
                .setSubject(payload.getUserName())
                .claim("userId", payload.getUserId())
                .claim("permissions", payload.getPermissions())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    private Set<PermissionDto> extractPermissions(Claims claims){
        List<Map<String, String>> permissions = claims.get("permissions", List.class);

        return permissions.stream()
                .map(p -> PermissionDto.builder()
                        .name(p.get("name"))
                        .action(p.get("action"))
                        .build())
                .collect(Collectors.toSet());
    }

    public ITokenPayload getPayloadFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        Set<PermissionDto> permissions = extractPermissions(claims);

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

    public boolean validateToken(String token){
        try{
           Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
           return true;
        }catch (Exception e){
            return false;
        }
    }
}
