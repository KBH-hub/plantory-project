package com.zero.plantory.domain.question.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SelectQuestionListVO {
    private Long questionId;
    private String title;
    private String nickname;
    private String createdAt;
    private Integer answerCount;
    private String imageUrl;
}
