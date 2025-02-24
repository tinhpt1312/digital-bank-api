package org.tinhpt.digital.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.jwt")
@Data
public class JwtConfig {
    private String secret;
    private long expiration;
    private String tokenPrefix;
    private String headerString;
}
