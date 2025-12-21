package com.github.maximovj.rhhub_app.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "app.seeder")
public class SeederProperties {
    
    private boolean enabled;
    
}
