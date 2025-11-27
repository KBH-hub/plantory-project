package com.zero.plantory.domain.plantingCalendar.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPlantDiaryResponse {
    private Long myplantId;
    private String name;
}
