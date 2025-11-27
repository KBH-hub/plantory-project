package com.zero.plantory.domain.plantingCalendar.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiaryResponse {
    Long diaryId;
    Long myplantId;
    String activity;
    String state;
    String memo;
    LocalDateTime createdAt;
}
