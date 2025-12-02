package com.zero.plantory.domain.question.service;

import com.zero.plantory.domain.image.mapper.ImageMapper;
import com.zero.plantory.domain.notice.NoticeMapper;
import com.zero.plantory.domain.question.dto.AnswerRequest;
import com.zero.plantory.domain.question.dto.QuestionRequest;
import com.zero.plantory.domain.question.dto.SelectQuestionDetailResponse;
import com.zero.plantory.domain.question.mapper.QuestionMapper;
import com.zero.plantory.global.utils.StorageUploader;
import com.zero.plantory.global.dto.ImageTargetType;
import com.zero.plantory.global.dto.ImageDTO;
import com.zero.plantory.global.dto.NoticeTargetType;
import com.zero.plantory.global.dto.NoticeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionWriteServiceImpl implements QuestionWriteService {

    private final QuestionMapper questionMapper;
    private final ImageMapper imageMapper;
    private final StorageUploader storageUploader;
    private final NoticeMapper noticeMapper;


    @Override
    @Transactional
    public Long registerQuestion(QuestionRequest request, List<MultipartFile> images) throws IOException {

        if (request.getTitle() == null || request.getTitle().isBlank())
            throw new IllegalArgumentException("제목은 필수입니다.");

        if (request.getContent() == null || request.getContent().isBlank())
            throw new IllegalArgumentException("내용은 필수입니다.");

        List<ImageDTO> imageList = new ArrayList<>();

        if (images != null) {
            for (MultipartFile file : images) {

                if (file.isEmpty()) continue;

                String url = storageUploader.uploadFile(file);

                imageList.add(
                        ImageDTO.builder()
                                .memberId(request.getMemberId())
                                .targetType(ImageTargetType.QUESTION)
                                .fileUrl(url)
                                .fileName(file.getOriginalFilename())
                                .build()
                );
            }
        }

        questionMapper.insertQuestion(request);
        Long questionId = request.getQuestionId();

        for (ImageDTO img : imageList) {
            img.setTargetId(questionId);
            imageMapper.insertImage(img);
        }

        return questionId;
    }


    @Override
    @Transactional
    public boolean updateQuestion(QuestionRequest request, Long loginMemberId, List<MultipartFile> newImages) throws IOException {

        if (!loginMemberId.equals(request.getMemberId()))
            throw new IllegalStateException("본인 글만 수정 가능");

        if (questionMapper.countMyQuestion(request) == 0)
            throw new IllegalStateException("수정 권한 없음");

        Long questionId = request.getQuestionId();

        if (request.getDeletedImageIdList() != null && !request.getDeletedImageIdList().isEmpty()) {
            for (Long id : request.getDeletedImageIdList()) {
                imageMapper.softDeleteImage(id);
            }
        }

        if (newImages != null && !newImages.isEmpty()) {
            for (MultipartFile file : newImages) {

                if (file.isEmpty()) continue;

                String url = storageUploader.uploadFile(file);

                ImageDTO img = ImageDTO.builder()
                        .memberId(request.getMemberId())
                        .targetType(ImageTargetType.QUESTION)
                        .targetId(questionId)
                        .fileUrl(url)
                        .fileName(file.getOriginalFilename())
                        .build();

                imageMapper.insertImage(img);
            }
        }

        return questionMapper.updateQuestion(request) > 0;
    }

    @Override
    @Transactional
    public boolean deleteQuestion(Long questionId, Long loginMemberId) {

        QuestionRequest request = QuestionRequest.builder()
                .questionId(questionId)
                .memberId(loginMemberId)
                .build();

        if (questionMapper.countMyQuestion(request) == 0)
            throw new IllegalStateException("본인의 질문만 삭제할 수 있습니다.");

        imageMapper.softDeleteImagesByTarget(ImageTargetType.QUESTION, questionId);

        return questionMapper.deleteQuestion(questionId) > 0;
    }

    @Override
    @Transactional
    public boolean addAnswer(AnswerRequest request) {

        if (request.getWriterId() == null)
            throw new IllegalArgumentException("로그인 정보 없음");
        if (request.getContent() == null || request.getContent().isBlank())
            throw new IllegalArgumentException("내용은 필수입니다.");

        int inserted = questionMapper.insertAnswer(request);

        if (inserted > 0) {
            SelectQuestionDetailResponse question = questionMapper.selectQuestionDetail(request.getQuestionId());
            Long ownerId = question.getMemberId();

            if (!request.getWriterId().equals(ownerId)) {
                NoticeDTO notice = NoticeDTO.builder()
                        .receiverId(ownerId)
                        .targetId(request.getQuestionId())
                        .targetType(NoticeTargetType.QUESTION)
                        .content("새 답글 알림 | 제목: " + question.getTitle())
                        .build();

                noticeMapper.insertNotice(notice);
            }
        }

        return inserted > 0;
    }

    @Override
    @Transactional
    public boolean updateAnswer(AnswerRequest request, Long loginMemberId) {

        if (!request.getWriterId().equals(loginMemberId))
            throw new IllegalStateException("본인의 답변만 수정 가능");

        if (questionMapper.countMyAnswer(request) == 0)
            throw new IllegalStateException("수정 권한 없음");

        return questionMapper.updateAnswerById(request) > 0;
    }

    @Override
    @Transactional
    public boolean deleteAnswer(AnswerRequest request, Long loginMemberId) {

        if (!request.getWriterId().equals(loginMemberId))
            throw new IllegalStateException("본인의 답변만 삭제 가능");

        if (questionMapper.countMyAnswer(request) == 0)
            throw new IllegalStateException("삭제 권한 없음");

        return questionMapper.deleteAnswer(request) > 0;
    }
}
