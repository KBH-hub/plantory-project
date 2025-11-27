package com.zero.plantory.domain.member.mapper;

import com.zero.plantory.domain.member.dto.MyWrittenDeleteRequest;
import com.zero.plantory.domain.member.dto.MyWrittenListRequest;
import com.zero.plantory.domain.member.dto.MyWrittenListResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MyContentMapper {
    List<MyWrittenListResponse> selectMyWrittenListAll(MyWrittenListRequest request);
    List<MyWrittenListResponse> selectMyWrittenListSharing(MyWrittenListRequest request);
    List<MyWrittenListResponse> selectMyWrittenListQuestion(MyWrittenListRequest request);

    int deleteMyWrittenSharing(MyWrittenDeleteRequest request);
    int deleteMyWrittenQuestion(MyWrittenDeleteRequest request);

    List<MyWrittenListResponse> selectMyCommentSearchAll(MyWrittenListRequest request);
    List<MyWrittenListResponse> selectMyCommentSearchSharing(MyWrittenListRequest request);
    List<MyWrittenListResponse> selectMyCommentSearchQuestion(MyWrittenListRequest request);
}

