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
public class AnswerVO {
    Long answerId;
    Long questionId;
    Long writerId;
    String content;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime delFlag;
}
