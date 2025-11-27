package com.zero.plantory.domain.question.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SelectAnswerListResponse {
    private Long answerId;
    private Long writerId;
    private String nickname;
    private String content;
    private String createdAt;
    private String updatedAt;
}
