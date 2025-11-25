// com.zero.plantory.global.utils.SolapiJsonLoader
package com.zero.plantory.global.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zero.plantory.global.config.SolapiConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
public class SolapiJsonLoader {

    @Bean
    public SolapiConfig solapiConfig() throws Exception {
        String path = System.getenv("SOLAPI_JSON_PATH");
        if (path == null || path.isBlank()) {
            throw new IllegalStateException("환경변수 SOLAPI_JSON_PATH 미설정");
        }
        JsonNode node = new ObjectMapper().readTree(Files.readString(Path.of(path)));
        String apiKey = req(node, "SOLAPI_API_KEY").trim();
        String apiSecret = req(node, "SOLAPI_API_SECRET").trim();
        String from = req(node, "SMS_FROM").trim(); ;
        if (apiKey.trim().length() != 16) {
            throw new IllegalStateException("SOLAPI_API_KEY 길이 16자 아님: " + apiKey.trim().length());
        }
        return new SolapiConfig(apiKey, apiSecret, from);
    }

    private static String req(JsonNode root, String key) {
        var v = root.get(key);
        if (v == null || v.asText().isBlank()) {
            throw new IllegalStateException("JSON에 필수 키 누락: " + key);
        }
        return v.asText();
    }
}
