package com.zero.plantory.domain.dashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendedSharingDTO {
    private Long sharingId;
    private String title;
    private String status;
    private LocalDateTime createdAt;
    private Integer interestNum;
    private Integer commentCount;
    private String fileUrl;
}

