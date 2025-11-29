package com.zero.plantory.domain.myProfile.service;

import com.zero.plantory.domain.myProfile.dto.MyWrittenDeleteRequest;
import com.zero.plantory.domain.myProfile.dto.MyWrittenListRequest;
import com.zero.plantory.domain.myProfile.dto.MyWrittenListResponse;

import java.util.List;

public interface MyContentService {

    List<MyWrittenListResponse> getMyWrittenListAll(MyWrittenListRequest request);
    List<MyWrittenListResponse> getMyWrittenListSharing(MyWrittenListRequest request);
    List<MyWrittenListResponse> getMyWrittenListQuestion(MyWrittenListRequest request);

    boolean deleteMyWrittenSharing(MyWrittenDeleteRequest request);
    boolean deleteMyWrittenQuestion(MyWrittenDeleteRequest request);

    List<MyWrittenListResponse> searchMyCommentAll(MyWrittenListRequest request);
    List<MyWrittenListResponse> searchMyCommentSharing(MyWrittenListRequest request);
    List<MyWrittenListResponse> searchMyCommentQuestion(MyWrittenListRequest request);
}
