package com.zero.plantory.global.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zero.plantory.global.config.SolapiConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
@EnableConfigurationProperties(SolapiConfig.class)
public class SolapiJsonLoader {

    @Bean
    public SolapiConfig SolapiConfig() throws IOException {
        var path = System.getenv("SOLAPI_JSON_PATH");
        if (path == null || path.isBlank()) {
            throw new IllegalStateException("환경변수 SOLAPI_JSON_PATH 미설정");
        }
        String json = Files.readString(Path.of(path));
        JsonNode node = new ObjectMapper().readTree(json);

        var apiKey = req(node, "SOLAPI_API_KEY");
        var apiSecret = req(node, "SOLAPI_API_SECRET");

        return new SolapiConfig(apiKey, apiSecret);
    }

    private static String req(JsonNode root, String key) {
        var v = root.get(key);
        if (v == null || v.asText().isBlank()) {
            throw new IllegalStateException("JSON에 필수 키 누락: " + key);
        }
        return v.asText();
    }
}