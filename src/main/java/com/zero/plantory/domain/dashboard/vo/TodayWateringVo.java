package com.zero.plantory.domain.dashboard.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TodayWateringVo {
    private String name;
    private Integer interval;
}
