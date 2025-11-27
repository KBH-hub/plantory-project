package com.zero.plantory.domain.plantingCalendar.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zero.plantory.domain.plantingCalendar.auth.SolapiAuth;
import com.zero.plantory.domain.plantingCalendar.dto.SMSRequestDTO;
import com.zero.plantory.global.config.SolapiConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class SMSServiceImpl implements SMSService {

    private final SolapiConfig solapi;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Map<String,Object> sendSMS(SMSRequestDTO request) throws Exception {
        String auth = SolapiAuth.createAuthHeader(solapi.apiKey(), solapi.apiSecret());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", auth);

        Map<String,Object> body = Map.of("message", Map.of(
                "to", request.getTo(), "from", request.getFrom(), "text", request.getText()
        ));
        var entity = new HttpEntity<>(body, headers);
        var resp = restTemplate.exchange("https://api.solapi.com/messages/v4/send",
                HttpMethod.POST, entity, String.class);
        return objectMapper.readValue(resp.getBody(), Map.class);
    }
}
