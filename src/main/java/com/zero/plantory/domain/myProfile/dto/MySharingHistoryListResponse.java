package com.zero.plantory.domain.myProfile.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MySharingHistoryListResponse {
    private Long sharingId;
    private String title;
    private String status;
    private Integer interestNum;
    private LocalDateTime createdAt;
    private LocalDateTime reviewFlag;
    private Integer commentCount;
    private String thumbnail;
    private  int totalCount;
}
