package com.github.maximovj.rhhub_app.config.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "app.cors")
public class CorsProperties {
    
    private List<String> allowedOrigins;

    private List<String> allowedMethods;
    
    private List<String> allowedHeaders;
    
    private Boolean allowCredentials;
    
    private String pathPattern;

}
