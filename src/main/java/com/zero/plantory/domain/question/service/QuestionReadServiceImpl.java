package com.zero.plantory.domain.question.service;

import com.zero.plantory.domain.image.mapper.ImageMapper;
import com.zero.plantory.domain.question.dto.QuestionListPageResponse;
import com.zero.plantory.domain.question.dto.SelectAnswerListResponse;
import com.zero.plantory.domain.question.dto.SelectQuestionDetailResponse;
import com.zero.plantory.domain.question.dto.SelectQuestionListResponse;
import com.zero.plantory.domain.question.mapper.QuestionMapper;
import com.zero.plantory.global.dto.ImageTargetType;
import com.zero.plantory.global.dto.ImageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionReadServiceImpl implements QuestionReadService {
    private final QuestionMapper questionMapper;
    private final ImageMapper imageMapper;

    @Override
    public QuestionListPageResponse getQuestionList(String keyword, int page, int size) {

        int limit = size;
        int offset = (page - 1) * size;

        List<SelectQuestionListResponse> list = questionMapper.selectQuestionList(keyword, limit, offset);

        int totalCount = questionMapper.selectQuestionTotalCount(keyword);

        return QuestionListPageResponse.builder()
                .list(list)
                .totalCount(totalCount)
                .page(page)
                .size(size)
                .build();
    }

    @Override
    public SelectQuestionDetailResponse getQuestionDetail(Long questionId) {

        SelectQuestionDetailResponse vo = questionMapper.selectQuestionDetail(questionId);

        if (vo == null) {
            throw new IllegalArgumentException("존재하지 않는 질문입니다.");
        }

        List<ImageDTO> images = imageMapper.selectImagesByTarget(ImageTargetType.QUESTION, questionId);

        vo.setImages(images);

        return vo;
    }

    @Override
    public List<SelectAnswerListResponse> getAnswerList(Long questionId) {
        return questionMapper.selectQuestionAnswers(questionId);
    }
}
