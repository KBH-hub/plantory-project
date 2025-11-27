package com.zero.plantory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PlantoryProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlantoryProjectApplication.class, args);
    }

}
