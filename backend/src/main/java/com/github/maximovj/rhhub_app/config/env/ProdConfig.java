package com.github.maximovj.rhhub_app.config.env;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import jakarta.annotation.PostConstruct;

@Profile("prod")
@Configuration
public class ProdConfig {

    private static Logger logger = LoggerFactory.getLogger(ProdConfig.class);

    @PostConstruct
    public void init() {
        PrintMessages.logger = logger;
        PrintMessages.show("PRODUCCION", null);
    }

}