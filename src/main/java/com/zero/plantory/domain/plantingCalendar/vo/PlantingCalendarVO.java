package com.zero.plantory.domain.plantingCalendar.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlantingCalendarVO {
    private Long wateringId;
    private Long myplantId;
    private Date dateAt;
    private Date checkFlag;
}
