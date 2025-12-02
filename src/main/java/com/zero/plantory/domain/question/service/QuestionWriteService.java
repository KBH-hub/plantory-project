package com.zero.plantory.domain.question.service;

import com.zero.plantory.domain.question.dto.AnswerRequest;
import com.zero.plantory.domain.question.dto.QuestionRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface QuestionWriteService {
    Long registerQuestion(QuestionRequest request, List<MultipartFile> images) throws IOException;

    boolean updateQuestion(QuestionRequest request, Long loginMemberId, List<MultipartFile> newImages) throws IOException;

    boolean deleteQuestion(Long questionId, Long loginMemberId);

    boolean addAnswer(AnswerRequest request);

    boolean updateAnswer(AnswerRequest request, Long loginMemberId);

    boolean deleteAnswer(AnswerRequest request, Long loginMemberId);
}

