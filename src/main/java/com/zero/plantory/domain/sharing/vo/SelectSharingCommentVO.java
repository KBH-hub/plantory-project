package com.zero.plantory.domain.sharing.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SelectSharingCommentVO {
    private Long commentId;
    private Long writerId;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

