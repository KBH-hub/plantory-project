package com.zero.plantory.domain.question.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SelectQuestionListResponse {
    private Long questionId;
    private String title;
    private String nickname;
    private String createdAt;
    private Integer answerCount;
    private String imageUrl;
}
