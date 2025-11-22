package com.zero.plantory.domain.question.mapper;

import com.zero.plantory.domain.question.vo.SelectAnswerListVO;
import com.zero.plantory.domain.question.vo.SelectQuestionDetailVO;
import com.zero.plantory.domain.question.vo.SelectQuestionListVO;
import com.zero.plantory.global.vo.AnswerVO;
import com.zero.plantory.global.vo.QuestionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface QuestionMapper {
    List<SelectQuestionListVO> selectQuestionList(@Param("keyword") String keyword, @Param("limit") Integer limit, @Param("offset") Integer offset);
    int insertQuestion(QuestionVO vo);
    SelectQuestionDetailVO selectQuestionDetail(@Param("questionId") Long questionId);
    List<SelectAnswerListVO> selectQuestionAnswers(@Param("questionId") Long questionId);

    int insertAnswer(AnswerVO vo);
    int countMyAnswer(AnswerVO vo);
    int updateAnswerById(AnswerVO vo);
    int deleteAnswer(AnswerVO vo);

    int countMyQuestion(QuestionVO vo);
    int updateQuestion(QuestionVO vo);

    int deleteQuestion(@Param("questionId") Long questionId);
}
