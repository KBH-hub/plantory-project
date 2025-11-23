package com.zero.plantory.domain.member.mapper;

import com.zero.plantory.domain.member.vo.MyWrittenDeleteRequestVO;
import com.zero.plantory.domain.member.vo.MyWrittenListRequestVO;
import com.zero.plantory.domain.member.vo.MyWrittenListVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MyContentMapper {
    List<MyWrittenListVO> selectMyWrittenListAll(MyWrittenListRequestVO request);
    List<MyWrittenListVO> selectMyWrittenListSharing(MyWrittenListRequestVO request);
    List<MyWrittenListVO> selectMyWrittenListQuestion(MyWrittenListRequestVO request);

    int deleteMyWrittenSharing(MyWrittenDeleteRequestVO request);
    int deleteMyWrittenQuestion(MyWrittenDeleteRequestVO request);

    List<MyWrittenListVO> selectMyCommentSearchAll(MyWrittenListRequestVO request);
    List<MyWrittenListVO> selectMyCommentSearchSharing(MyWrittenListRequestVO request);
    List<MyWrittenListVO> selectMyCommentSearchQuestion(MyWrittenListRequestVO request);
}

