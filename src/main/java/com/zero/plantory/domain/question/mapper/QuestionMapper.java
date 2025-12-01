package com.zero.plantory.domain.question.mapper;

import com.zero.plantory.domain.question.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QuestionMapper {
    List<SelectQuestionListResponse> selectQuestionList(@Param("keyword") String keyword, @Param("limit") Integer limit, @Param("offset") Integer offset);
    int selectQuestionTotalCount(@Param("keyword") String keyword);
    int insertQuestion(QuestionRequest request);
    SelectQuestionDetailResponse selectQuestionDetail(@Param("questionId") Long questionId);
    List<SelectAnswerListResponse> selectQuestionAnswers(@Param("questionId") Long questionId);

    int insertAnswer(AnswerRequest request);
    int countMyAnswer(AnswerRequest request);
    int updateAnswerById(AnswerRequest request);
    int deleteAnswer(AnswerRequest request);

    int countMyQuestion(QuestionRequest request);
    int updateQuestion(QuestionRequest request);

    int deleteQuestion(@Param("questionId") Long questionId);
}
