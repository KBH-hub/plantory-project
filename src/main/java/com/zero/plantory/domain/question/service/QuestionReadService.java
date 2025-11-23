package com.zero.plantory.domain.question.service;

import com.zero.plantory.domain.question.vo.SelectAnswerListVO;
import com.zero.plantory.domain.question.vo.SelectQuestionDetailVO;
import com.zero.plantory.domain.question.vo.SelectQuestionListVO;

import java.util.List;

public interface QuestionReadService {
    List<SelectQuestionListVO> getQuestionList(String keyword, Integer limit, Integer offset);

    SelectQuestionDetailVO getQuestionDetail(Long questionId);

    List<SelectAnswerListVO> getAnswerList(Long questionId);
}
