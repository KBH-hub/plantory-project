package com.zero.plantory.domain.profile.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileSharingHistoryListResponse {
    private Long sharingId;
    private String title;
    private String status;
    private Integer interestNum;
    private LocalDateTime createdAt;
    private LocalDateTime reviewFlag;
    private Integer commentCount;
    private String thumbnail;
}
