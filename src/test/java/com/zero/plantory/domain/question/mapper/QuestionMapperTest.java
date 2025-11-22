package com.zero.plantory.domain.question.mapper;

import com.zero.plantory.domain.question.vo.SelectQuestionDetailVO;
import com.zero.plantory.global.vo.AnswerVO;
import com.zero.plantory.global.vo.QuestionVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class QuestionMapperTest {

    @Autowired
    private QuestionMapper mapper;

    @Test
    @DisplayName("질문 삭제")
    void deleteQuestionTest(){
        log.info("삭제 결과 = {}", mapper.deleteQuestion(20L));
    }

    @Test
    @DisplayName("질문 수정 권한 체크")
    void countMyQuestionTest() {

        QuestionVO vo = QuestionVO.builder()
                .questionId(21L)
                .memberId(1L)
                .build();

        log.info("수정 권한 체크 count = {}", mapper.countMyQuestion(vo));
    }

    @Test
    @DisplayName("질문 수정")
    void updateQuestionTest() {

        QuestionVO vo = QuestionVO.builder()
                .questionId(21L)
                .memberId(1L)
                .title("테스트")
                .content("질문수정테스트")
                .build();

        log.info("질문 수정 결과 = {}", mapper.updateQuestion(vo));
    }

    @Test
    @DisplayName("답글 수정 및 삭제 권한 체크")
    void countMyAnswerTest() {

        AnswerVO vo = AnswerVO.builder()
                .answerId(1L)
                .questionId(21L)
                .writerId(1L)
                .build();

        log.info("권한 체크 결과 count = {}", mapper.countMyAnswer(vo));
    }


    @Test
    @DisplayName("답글 수정 처리")
    void updateAnswerTest() {

        AnswerVO vo = AnswerVO.builder()
                .answerId(1L)
                .questionId(21L)
                .writerId(1L)
                .content("수정 테스트.")
                .build();

        log.info("답글 수정 결과 = {}", mapper.updateAnswerById(vo));
    }


    @Test
    @DisplayName("답글 삭제 처리")
    void deleteAnswerTest() {

        AnswerVO vo = AnswerVO.builder()
                .answerId(1L)
                .questionId(21L)
                .writerId(1L)
                .build();

        log.info("답글 삭제 결과 = {}", mapper.deleteAnswer(vo));
    }

    @Test
    @DisplayName("답글 등록")
    void insertAnswerTest() {

        AnswerVO vo = AnswerVO.builder()
                .questionId(21L)
                .writerId(1L)
                .content("답글 등록 테스트")
                .build();

        log.info("답글 등록 결과 = {}", mapper.insertAnswer(vo));  // 1 → 성공
    }

    @Test
    @DisplayName("질문 답글 상세 조회")
    void selectAnswerListTest(){
        log.info("질문 답글 상세 = {}", mapper.selectQuestionAnswers(2L));
    }

    @Test
    @DisplayName("질문 상세 조회")
    void selectQuestionDetailTest(){
        log.info("질문 상세 = {}", mapper.selectQuestionDetail(2L));
    }

    @Test
    @DisplayName("질문 등록")
    void insertQuestionTest() {
        QuestionVO vo = QuestionVO.builder()
                .memberId(1L)
                .title("금전수 잎 끝이 노래져요")
                .content("최근에 금전수 잎 끝이 조금씩 노래지는데 물주기나 햇빛량을 어떻게 조절해야 할지 궁금합니다.")
                .build();

        log.info("질문 등록 결과 = {}", mapper.insertQuestion(vo));
    }

    @Test
    @DisplayName("질문 리스트 검색 테스트")
    void selectQuestionListTest() {
        mapper.selectQuestionList("", 8, 0)
                .forEach(item -> log.info("검색 결과 = {}", item));
    }
}
