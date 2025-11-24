package com.zero.plantory;

import com.zero.plantory.global.config.SolapiConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(SolapiConfig.class)
public class PlantoryProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlantoryProjectApplication.class, args);
    }

}
