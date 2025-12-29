package com.github.maximovj.rhhub_app.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Setter
@Getter
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

    private String secretKey;

    private Long expirationTime;

    private Long refreshExpiration;
    
}
