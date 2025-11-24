package com.zero.plantory.domain.plantingCalendar.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SMSRequestDTO {
    private String to;
    private String from;
    private String text;
}
