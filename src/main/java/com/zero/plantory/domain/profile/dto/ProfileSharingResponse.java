package com.zero.plantory.domain.profile.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProfileSharingResponse {
    private Long sharingId;
    private String title;
    private String status;
    private Integer interestNum;
    private LocalDateTime createdAt;
    private Integer commentCount;
    private String thumbnail;
    private int totalCount;
}