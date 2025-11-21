package com.zero.plantory.domain.dashboard.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RecommendedSharingVO {
    private Long sharingId;
    private String title;
    private String status;
    private LocalDateTime createdAt;
    private Integer interestNum;
    private Integer commentCount;
    private String fileUrl;
}

