package com.zero.plantory.domain.member.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MySharingHistoryResponse {
    private Long sharingId;
    private String title;
    private String status;
    private Integer interestNum;
    private LocalDateTime createdAt;
    private LocalDateTime reviewFlag;
    private Integer commentCount;
    private String thumbnail;
}
