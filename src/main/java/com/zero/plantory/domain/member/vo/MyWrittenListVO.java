package com.zero.plantory.domain.member.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyWrittenListVO {
    private Long id;
    private Long targetId;
    private Long writerId;
    private Long memberId;
    private String nickname;
    private String title;
    private LocalDateTime createdAt;
    private String type;
    private String category;
}
