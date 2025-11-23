package com.zero.plantory.domain.question.service;

import com.zero.plantory.domain.image.ImageMapper;
import com.zero.plantory.domain.notice.NoticeMapper;
import com.zero.plantory.domain.question.mapper.QuestionMapper;
import com.zero.plantory.domain.question.vo.SelectQuestionDetailVO;
import com.zero.plantory.global.utils.StorageUploader;
import com.zero.plantory.global.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public Long registerQuestion(QuestionVO vo, List<MultipartFile> images) throws IOException {

        if (vo.getTitle() == null || vo.getTitle().isBlank())
            throw new IllegalArgumentException("제목은 필수입니다.");

        if (vo.getContent() == null || vo.getContent().isBlank())
            throw new IllegalArgumentException("내용은 필수입니다.");

        questionMapper.insertQuestion(vo);
        Long questionId = vo.getQuestionId();

        if (images != null) {
            for (MultipartFile file : images) {

                String url = storageUploader.uploadFile(file);

                ImageVO img = ImageVO.builder()
                        .memberId(vo.getMemberId())
                        .targetType(ImageTargetType.QUESTION)
                        .targetId(questionId)
                        .fileUrl(url)
                        .fileName(file.getOriginalFilename())
                        .build();

                imageMapper.insertImage(img);
            }
        }

        return questionId;
    }

    @Override
    @Transactional
    public boolean updateQuestion(QuestionVO vo, Long loginMemberId, List<MultipartFile> newImages) throws IOException {

        if (!loginMemberId.equals(vo.getMemberId()))
            throw new IllegalStateException("본인의 질문만 수정할 수 있습니다.");

        if (questionMapper.countMyQuestion(vo) == 0)
            throw new IllegalStateException("수정 권한 없음");

        Long questionId = vo.getQuestionId();

        if (newImages != null && !newImages.isEmpty()) {

            imageMapper.softDeleteImagesByTarget(ImageTargetType.QUESTION, questionId);

            for (MultipartFile file : newImages) {

                String url = storageUploader.uploadFile(file);

                ImageVO img = ImageVO.builder()
                        .memberId(vo.getMemberId())
                        .targetType(ImageTargetType.QUESTION)
                        .targetId(questionId)
                        .fileUrl(url)
                        .fileName(file.getOriginalFilename())
                        .build();

                imageMapper.insertImage(img);
            }
        }

        return questionMapper.updateQuestion(vo) > 0;
    }

    @Override
    @Transactional
    public boolean deleteQuestion(Long questionId, Long loginMemberId) {

        QuestionVO vo = QuestionVO.builder()
                .questionId(questionId)
                .memberId(loginMemberId)
                .build();

        if (questionMapper.countMyQuestion(vo) == 0)
            throw new IllegalStateException("본인의 질문만 삭제할 수 있습니다.");

        imageMapper.softDeleteImagesByTarget(ImageTargetType.QUESTION, questionId);

        return questionMapper.deleteQuestion(questionId) > 0;
    }

    @Override
    @Transactional
    public boolean addAnswer(AnswerVO vo) {

        if (vo.getWriterId() == null)
            throw new IllegalArgumentException("로그인 정보 없음");
        if (vo.getContent() == null || vo.getContent().isBlank())
            throw new IllegalArgumentException("내용은 필수입니다.");

        int inserted = questionMapper.insertAnswer(vo);

        if (inserted > 0) {
            SelectQuestionDetailVO question = questionMapper.selectQuestionDetail(vo.getQuestionId());
            Long ownerId = question.getMemberId();

            if (!vo.getWriterId().equals(ownerId)) {
                NoticeVO notice = NoticeVO.builder()
                        .receiverId(ownerId)
                        .targetId(vo.getQuestionId())
                        .targetType(NoticeTargetType.QUESTION)
                        .content("새로운 답변이 등록되었습니다!")
                        .build();

                noticeMapper.insertNotice(notice);
            }
        }

        return inserted > 0;
    }

    @Override
    @Transactional
    public boolean updateAnswer(AnswerVO vo, Long loginMemberId) {

        if (!vo.getWriterId().equals(loginMemberId))
            throw new IllegalStateException("본인의 답변만 수정 가능");

        if (questionMapper.countMyAnswer(vo) == 0)
            throw new IllegalStateException("수정 권한 없음");

        return questionMapper.updateAnswerById(vo) > 0;
    }

    @Override
    @Transactional
    public boolean deleteAnswer(AnswerVO vo, Long loginMemberId) {

        if (!vo.getWriterId().equals(loginMemberId))
            throw new IllegalStateException("본인의 답변만 삭제 가능");

        if (questionMapper.countMyAnswer(vo) == 0)
            throw new IllegalStateException("삭제 권한 없음");

        return questionMapper.deleteAnswer(vo) > 0;
    }
}
