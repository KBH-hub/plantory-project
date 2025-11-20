package com.zero.plantory.domain.dashboard.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodayDiaryVo {
    private Long diaryId;
    private Long myplantId;
    private String myplantName;
    private String activity;
    private String state;
    private String memo;
    private String createdAt;
    private String imageUrl;
}
