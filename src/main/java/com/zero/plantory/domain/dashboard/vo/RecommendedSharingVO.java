package com.zero.plantory.domain.dashboard.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RecommendedSharingVO {
    private Long sharingId;
    private String title;
    private String status;
    private String createdAt;   // 또는 LocalDateTime
    private Integer interestNum;
    private Integer commentCount;
    private String fileUrl;
}

