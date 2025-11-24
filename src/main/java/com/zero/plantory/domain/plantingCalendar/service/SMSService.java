package com.zero.plantory.domain.plantingCalendar.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.zero.plantory.domain.plantingCalendar.dto.SMSRequestDTO;

import java.util.Map;

public interface SMSService {
    Map<String, Object> sendSMS(SMSRequestDTO smsRequestDTO) throws Exception;
}
