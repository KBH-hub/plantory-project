package com.zero.plantory.domain.question.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SelectQuestionDetailVO {
    private Long questionId;
    private Long memberId;
    private String nickname;
    private String title;
    private String content;
    private String createdAt;
    private String updatedAt;
}
