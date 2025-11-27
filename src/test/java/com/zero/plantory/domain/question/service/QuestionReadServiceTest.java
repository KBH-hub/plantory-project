package com.zero.plantory.domain.question.service;

import com.zero.plantory.domain.question.dto.SelectQuestionDetailResponse;
import com.zero.plantory.domain.question.dto.SelectQuestionListResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class QuestionReadServiceTest {

    @Autowired
    private QuestionReadService questionReadService;

    @Test
    @DisplayName("질문 목록 조회")
    void selectQuestionListTest() {
        List<SelectQuestionListResponse> result = questionReadService.getQuestionList("", 10, 0);
        log.info("조회 결과 = {}", result);
    }

    @Test
    @DisplayName("질문 상세 + 이미지 조회")
    void questionDetailTest() {

        Long questionId = 2L;

        SelectQuestionDetailResponse vo = questionReadService.getQuestionDetail(questionId);

        log.info("질문 상세 = {}", vo);
        log.info("이미지 리스트 = {}", vo.getImages());
    }

    @Test
    @DisplayName("답변 목록 조회")
    void selectAnswerTest() {
        Long questionId = 1L;
        log.info("답변 목록 = {}", questionReadService.getAnswerList(questionId));
    }
}
