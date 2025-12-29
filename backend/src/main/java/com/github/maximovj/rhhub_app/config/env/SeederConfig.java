package com.github.maximovj.rhhub_app.config.env;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.github.maximovj.rhhub_app.config.properties.DataSourceProperties;
import com.github.maximovj.rhhub_app.config.properties.SeederProperties;

import jakarta.annotation.PostConstruct;

@Profile("seeder")
@Configuration
public class SeederConfig {

    private static Logger logger = LoggerFactory.getLogger(SeederConfig.class);

    @Autowired
    SeederProperties seederProperties;

    @Autowired
    DataSourceProperties dataSourceProperties;

    @PostConstruct
    public void init() {
        PrintMessages.logger = logger;
        PrintMessages.show("seeder", Map.of(
            "Seeder activado ?", seederProperties.isEnabled(),
            "Datasource URL", dataSourceProperties.getUrl()
        ));
    }
    
}
