package com.github.maximovj.rhhub_app.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "app.cookie.refresh-token")
public class CookieRefreshTokenProperties {

    private String name;

    private int maxAge;
    
    private String path;
    
    private boolean httpOnly;
    
    private boolean secure;
    
    private String sameSite;
    
}
