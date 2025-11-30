package com.zero.plantory.domain.sharing.dto;

import com.zero.plantory.domain.sharing.service.SharingScoreServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewInfoResponse {
    private Long sharingId;
    private Long partnerId;
    private String partnerNickname;
    private String title;
    private LocalDateTime createdAt;
    private SharingScoreServiceImpl.ReviewerType reviewerType;
}