package com.zero.plantory.domain.plantingCalendar.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyplantSlotBaseVO {
    private Long myplantId;
    private LocalDateTime startAt;
    private LocalDateTime endDate;
    private Integer interval;
    private String phone;
    private String name;
}
