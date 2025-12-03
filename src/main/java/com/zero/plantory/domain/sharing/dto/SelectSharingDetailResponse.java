package com.zero.plantory.domain.sharing.dto;

import com.zero.plantory.global.dto.ImageDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SelectSharingDetailResponse {
    private Long sharingId;
    private Long memberId;
    private Long targetMemberId;
    private String nickname;
    private BigDecimal sharingRate;

    private String title;
    private String content;
    private String plantType;
    private String managementLevel;
    private String managementNeeds;
    private String managementLevelLabel;
    private String managementNeedsLabel;
    private Integer interestNum;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<ImageDTO> images;
    private boolean isInterested;

    private LocalDateTime reviewFlag;
    private LocalDateTime receiverReviewFlag;
}
