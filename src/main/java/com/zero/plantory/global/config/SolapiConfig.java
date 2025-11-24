package com.zero.plantory.global.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "solapi")
public record SolapiConfig(String apiKey, String apiSecret) {}
