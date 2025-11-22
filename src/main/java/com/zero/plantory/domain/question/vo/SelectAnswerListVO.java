package com.zero.plantory.domain.question.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SelectAnswerListVO {
    private Long answerId;
    private Long writerId;
    private String nickname;
    private String content;
    private String createdAt;
    private String updatedAt;
}
