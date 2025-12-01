package com.zero.plantory.domain.question.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionListPageResponse {
    private List<SelectQuestionListResponse> list;
    private int totalCount;
    private int page;
    private int size;
}
