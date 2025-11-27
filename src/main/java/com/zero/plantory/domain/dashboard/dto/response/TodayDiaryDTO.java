package com.zero.plantory.domain.dashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodayDiaryDTO {
    private Long diaryId;
    private Long myplantId;
    private String myplantName;
    private String activity;
    private String state;
    private String memo;
    private String createdAt;
    private String fileUrl;
}
