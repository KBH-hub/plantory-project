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
public class DueWateringRowVO {
    private Long myplantId;
    private LocalDateTime nextAt;
}
