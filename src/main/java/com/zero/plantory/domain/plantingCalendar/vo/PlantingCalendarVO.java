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
    private Long id;           // diary_id / watering_id
    private String myplantName;// 식물 이름
    private String content;    // 다이어리 메모(물주기는 null)
    private String type;
}
