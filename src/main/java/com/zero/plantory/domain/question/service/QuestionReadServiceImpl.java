package com.zero.plantory.domain.question.service;

import com.zero.plantory.domain.image.ImageMapper;
import com.zero.plantory.domain.question.mapper.QuestionMapper;
import com.zero.plantory.domain.question.vo.SelectAnswerListVO;
import com.zero.plantory.domain.question.vo.SelectQuestionDetailVO;
import com.zero.plantory.domain.question.vo.SelectQuestionListVO;
import com.zero.plantory.global.vo.ImageTargetType;
import com.zero.plantory.global.vo.ImageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionReadServiceImpl implements QuestionReadService {
    private final QuestionMapper questionMapper;
    private final ImageMapper imageMapper;

    @Override
    public List<SelectQuestionListVO> getQuestionList(String keyword, Integer limit, Integer offset) {
        return questionMapper.selectQuestionList(keyword, limit, offset);
    }

    @Override
    public SelectQuestionDetailVO getQuestionDetail(Long questionId) {

        SelectQuestionDetailVO vo = questionMapper.selectQuestionDetail(questionId);

        if (vo == null) {
            throw new IllegalArgumentException("존재하지 않는 질문입니다.");
        }

        List<ImageVO> images = imageMapper.selectImagesByTarget(ImageTargetType.QUESTION, questionId);

        vo.setImages(images);

        return vo;
    }

    @Override
    public List<SelectAnswerListVO> getAnswerList(Long questionId) {
        return questionMapper.selectQuestionAnswers(questionId);
    }
}
