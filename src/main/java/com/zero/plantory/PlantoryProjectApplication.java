package com.zero.plantory;

import com.zero.plantory.global.config.SolapiConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PlantoryProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlantoryProjectApplication.class, args);
    }

}
