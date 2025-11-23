package com.zero.plantory.domain.member.service;

import com.zero.plantory.domain.member.vo.MyWrittenDeleteRequestVO;
import com.zero.plantory.domain.member.vo.MyWrittenListRequestVO;
import com.zero.plantory.domain.member.vo.MyWrittenListVO;

import java.util.List;

public interface MyContentService {

    List<MyWrittenListVO> getMyWrittenListAll(MyWrittenListRequestVO request);
    List<MyWrittenListVO> getMyWrittenListSharing(MyWrittenListRequestVO request);
    List<MyWrittenListVO> getMyWrittenListQuestion(MyWrittenListRequestVO request);

    boolean deleteMyWrittenSharing(MyWrittenDeleteRequestVO request);
    boolean deleteMyWrittenQuestion(MyWrittenDeleteRequestVO request);

    List<MyWrittenListVO> searchMyCommentAll(MyWrittenListRequestVO request);
    List<MyWrittenListVO> searchMyCommentSharing(MyWrittenListRequestVO request);
    List<MyWrittenListVO> searchMyCommentQuestion(MyWrittenListRequestVO request);
}
