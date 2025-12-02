package com.zero.plantory.domain.question.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zero.plantory.domain.question.dto.AnswerRequest;
import com.zero.plantory.domain.question.dto.QuestionRequest;
import com.zero.plantory.domain.question.service.QuestionWriteService;
import com.zero.plantory.global.security.MemberDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionWriteRestController {
    private final QuestionWriteService questionWriteService;

    @PostMapping
    public ResponseEntity<?> createQuestion(
            @ModelAttribute QuestionRequest request,
            @RequestParam(value = "files", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal MemberDetail memberDetail
    ) throws IOException {

        request.setMemberId(memberDetail.getMemberResponse().getMemberId());
        Long questionId = questionWriteService.registerQuestion(request, images);

        return ResponseEntity.ok(questionId);
    }

    @PutMapping("/{questionId}")
    public ResponseEntity<?> updateQuestion(
            @PathVariable Long questionId,
            @ModelAttribute QuestionRequest request,
            @RequestParam(value = "files", required = false) List<MultipartFile> newImages,
            @AuthenticationPrincipal MemberDetail memberDetail
    ) throws IOException {

        request.setQuestionId(questionId);
        Long loginMemberId = memberDetail.getMemberResponse().getMemberId();
        request.setMemberId(loginMemberId);

        if (request.getDeletedImageIds() != null && !request.getDeletedImageIds().isBlank()) {
            ObjectMapper mapper = new ObjectMapper();
            List<Long> ids = mapper.readValue(
                    request.getDeletedImageIds(),
                    new TypeReference<List<Long>>() {}
            );
            request.setDeletedImageIdList(ids);
        }

        boolean result = questionWriteService.updateQuestion(request, loginMemberId, newImages);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity<?> deleteQuestion(
            @PathVariable Long questionId,
            @AuthenticationPrincipal MemberDetail memberDetail
    ) {
        Long memberId = memberDetail.getMemberResponse().getMemberId();
        boolean result = questionWriteService.deleteQuestion(questionId, memberId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{questionId}/answers")
    public ResponseEntity<?> addAnswer(
            @PathVariable Long questionId,
            @RequestBody AnswerRequest request,
            @AuthenticationPrincipal MemberDetail memberDetail
    ) {

        request.setQuestionId(questionId);
        request.setWriterId(memberDetail.getMemberResponse().getMemberId());

        boolean result = questionWriteService.addAnswer(request);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/answers/{answerId}")
    public ResponseEntity<?> updateAnswer(
            @PathVariable Long answerId,
            @RequestBody AnswerRequest request,
            @AuthenticationPrincipal MemberDetail memberDetail
    ) {
        request.setAnswerId(answerId);
        Long loginId = memberDetail.getMemberResponse().getMemberId();
        request.setWriterId(loginId);
        boolean result = questionWriteService.updateAnswer(request, loginId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/answers/{answerId}")
    public ResponseEntity<?> deleteAnswer(
            @PathVariable Long answerId,
            @AuthenticationPrincipal MemberDetail memberDetail,
            @RequestBody AnswerRequest request
    ) {
        Long loginId = memberDetail.getMemberResponse().getMemberId();

        request.setAnswerId(answerId);
        request.setWriterId(loginId);

        boolean result = questionWriteService.deleteAnswer(request, loginId);
        return ResponseEntity.ok(result);
    }
}
