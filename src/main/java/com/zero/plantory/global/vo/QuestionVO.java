package com.zero.plantory.global.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionVO {
    Long questionId;
    Long memberId;
    String title;
    String content;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime delFlag;
}
