package com.zero.plantory.domain.question.service;

import com.zero.plantory.domain.question.dto.AnswerRequest;
import com.zero.plantory.domain.question.dto.QuestionRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface QuestionWriteService {
    /** 질문 등록 */
    Long registerQuestion(QuestionRequest request, List<MultipartFile> images) throws IOException;

    /** 질문 수정  */
    boolean updateQuestion(QuestionRequest request, Long loginMemberId, List<MultipartFile> newImages) throws IOException;

    /** 질문 삭제  */
    boolean deleteQuestion(Long questionId, Long loginMemberId);

    /** 답변 등록 + 알림 전송 */
    boolean addAnswer(AnswerRequest request);

    /** 답변 수정 */
    boolean updateAnswer(AnswerRequest request, Long loginMemberId);

    /** 답변 삭제 */
    boolean deleteAnswer(AnswerRequest request, Long loginMemberId);
}

