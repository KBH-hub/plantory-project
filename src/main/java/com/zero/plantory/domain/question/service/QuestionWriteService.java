package com.zero.plantory.domain.question.service;

import com.zero.plantory.global.vo.AnswerVO;
import com.zero.plantory.global.vo.QuestionVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface QuestionWriteService {
    /** 질문 등록 */
    Long registerQuestion(QuestionVO vo, List<MultipartFile> images) throws IOException;

    /** 질문 수정  */
    boolean updateQuestion(QuestionVO vo, Long loginMemberId, List<MultipartFile> newImages) throws IOException;

    /** 질문 삭제  */
    boolean deleteQuestion(Long questionId, Long loginMemberId);

    /** 답변 등록 + 알림 전송 */
    boolean addAnswer(AnswerVO vo);

    /** 답변 수정 */
    boolean updateAnswer(AnswerVO vo, Long loginMemberId);

    /** 답변 삭제 */
    boolean deleteAnswer(AnswerVO vo, Long loginMemberId);
}

