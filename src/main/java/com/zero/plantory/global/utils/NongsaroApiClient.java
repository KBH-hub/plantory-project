package com.zero.plantory.global.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class NongsaroApiClient {

    private final RestClient restClient;

    @Value("${openapi.nongsaro.key}")
    private String apiKey;

    public String get(String baseUrl, String path, Map<String, String> params) {

        return restClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path(baseUrl + path)
                            .queryParam("apiKey", apiKey);

                    if (params != null) {
                        params.forEach(builder::queryParam);
                    }

                    return builder.build();
                })
                .retrieve()
                .body(String.class);
    }
}