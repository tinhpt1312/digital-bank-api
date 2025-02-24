package org.tinhpt.digital.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "app.jwt")
public class JwtConfig {
    private String secret;
    private long expiration;
    private String tokenPrefix;
    private String headerString;

    public JwtConfig() {
    }

    public JwtConfig(String secret, long expiration, String tokenPrefix, String headerString) {
        this.secret = secret;
        this.expiration = expiration;
        this.tokenPrefix = tokenPrefix;
        this.headerString = headerString;
    }

}
