package com.zero.plantory.global.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

public record SolapiConfig(String apiKey, String apiSecret, String from) {}
