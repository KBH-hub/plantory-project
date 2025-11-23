package com.zero.plantory.domain.member.vo;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MySharingHistoryVO {
    private Long sharingId;
    private String title;
    private String status;
    private Integer interestNum;
    private LocalDateTime createdAt;
    private LocalDateTime reviewFlag;
    private Integer commentCount;
    private String thumbnail;
}
