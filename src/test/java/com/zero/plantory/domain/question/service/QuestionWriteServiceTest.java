package com.zero.plantory.domain.question.service;

import com.zero.plantory.domain.question.dto.AnswerRequest;
import com.zero.plantory.domain.question.dto.QuestionRequest;
import com.zero.plantory.global.vo.AnswerVO;
import com.zero.plantory.global.vo.QuestionVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)   // ⭐ 순서 강제
public class QuestionWriteServiceTest {

    @Autowired
    private QuestionWriteService questionWriteService;

    static Long createdQuestionId;
    static Long createdAnswerId;


    @Test
    @Order(1)
    @DisplayName("질문 등록 + 이미지 업로드")
    void registerQuestionTest() throws Exception {

        MockMultipartFile f1 = new MockMultipartFile(
                "file", "img1.png", "image/png", "img1".getBytes()
        );

        MockMultipartFile f2 = new MockMultipartFile(
                "file", "img2.png", "image/png", "img2".getBytes()
        );

        List<MultipartFile> files = List.of(f1, f2);

        QuestionRequest request = QuestionRequest.builder()
                .memberId(2L)
                .title("테스트 질문 제목")
                .content("테스트 질문 내용")
                .build();

        createdQuestionId = questionWriteService.registerQuestion(request, files);

        log.info("등록된 질문 ID = {}", createdQuestionId);
        Assertions.assertNotNull(createdQuestionId);
    }



    @Test
    @Order(2)
    @DisplayName("질문 수정 + 이미지 업데이트")
    void updateQuestionTest() throws Exception {

        MockMultipartFile updated = new MockMultipartFile(
                "file", "updated.png", "image/png", "updated image".getBytes()
        );

        QuestionRequest request = QuestionRequest.builder()
                .questionId(createdQuestionId)
                .memberId(2L)
                .title("수정된 제목")
                .content("수정된 내용")
                .build();

        boolean result = questionWriteService.updateQuestion(request, 2L, List.of(updated));

        log.info("수정 결과 = {}", result);
        Assertions.assertTrue(result);
    }



    @Test
    @Order(3)
    @DisplayName("답변 등록 + 알림")
    void addAnswerTest() {

        AnswerRequest request = AnswerRequest.builder()
                .questionId(createdQuestionId)
                .writerId(4L)
                .content("테스트 답변입니다.")
                .build();

        boolean result = questionWriteService.addAnswer(request);

        log.info("답변 등록 결과 = {}", result);
        Assertions.assertTrue(result);

        createdAnswerId = request.getAnswerId();
    }


    @Test
    @Order(4)
    @DisplayName("답변 수정")
    void updateAnswerTest() {

        AnswerRequest request = AnswerRequest.builder()
                .answerId(createdAnswerId)
                .questionId(createdQuestionId)
                .writerId(4L)
                .content("수정된 답변 내용입니다.")
                .build();

        boolean result = questionWriteService.updateAnswer(request, 4L);

        log.info("답변 수정 결과 = {}", result);
        Assertions.assertTrue(result);
    }



    @Test
    @Order(5)
    @DisplayName("답변 삭제")
    void deleteAnswerTest() {

        AnswerRequest request = AnswerRequest.builder()
                .answerId(createdAnswerId)
                .questionId(createdQuestionId)
                .writerId(4L)
                .build();

        boolean result = questionWriteService.deleteAnswer(request, 4L);

        log.info("답변 삭제 결과 = {}", result);
        Assertions.assertTrue(result);
    }



    @Test
    @Order(6)
    @DisplayName("질문 삭제")
    void deleteQuestionTest() {

        boolean result = questionWriteService.deleteQuestion(createdQuestionId, 2L);

        log.info("질문 삭제 결과 = {}", result);
        Assertions.assertTrue(result);
    }
}
