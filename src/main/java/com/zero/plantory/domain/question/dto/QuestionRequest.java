package com.zero.plantory.domain.question.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionRequest {
    Long questionId;
    Long memberId;
    String title;
    String content;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime delFlag;

    private String deletedImageIds;
    private List<Long> deletedImageIdList;
}
