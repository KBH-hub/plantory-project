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
    private Long memberId;
    private String name;
    private String content;
    private String type;
    private Date createdAt;
    private Date checkFlag;
}
