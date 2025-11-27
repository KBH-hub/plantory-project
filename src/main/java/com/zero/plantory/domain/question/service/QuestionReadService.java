package com.zero.plantory.domain.question.service;

import com.zero.plantory.domain.question.dto.SelectAnswerListResponse;
import com.zero.plantory.domain.question.dto.SelectQuestionDetailResponse;
import com.zero.plantory.domain.question.dto.SelectQuestionListResponse;

import java.util.List;

public interface QuestionReadService {
    List<SelectQuestionListResponse> getQuestionList(String keyword, Integer limit, Integer offset);

    SelectQuestionDetailResponse getQuestionDetail(Long questionId);

    List<SelectAnswerListResponse> getAnswerList(Long questionId);
}
