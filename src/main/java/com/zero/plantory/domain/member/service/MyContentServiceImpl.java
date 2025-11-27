package com.zero.plantory.domain.member.service;

import com.zero.plantory.domain.member.dto.MyWrittenDeleteRequest;
import com.zero.plantory.domain.member.dto.MyWrittenListRequest;
import com.zero.plantory.domain.member.dto.MyWrittenListResponse;
import com.zero.plantory.domain.member.mapper.MyContentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyContentServiceImpl implements MyContentService {

    private final MyContentMapper myContentMapper;

    @Override
    public List<MyWrittenListResponse> getMyWrittenListAll(MyWrittenListRequest request) {
        return myContentMapper.selectMyWrittenListAll(request);
    }

    @Override
    public List<MyWrittenListResponse> getMyWrittenListSharing(MyWrittenListRequest request) {
        return myContentMapper.selectMyWrittenListSharing(request);
    }

    @Override
    public List<MyWrittenListResponse> getMyWrittenListQuestion(MyWrittenListRequest request) {
        return myContentMapper.selectMyWrittenListQuestion(request);
    }

    @Override
    @Transactional
    public boolean deleteMyWrittenSharing(MyWrittenDeleteRequest request) {
        return myContentMapper.deleteMyWrittenSharing(request) > 0;
    }

    @Override
    @Transactional
    public boolean deleteMyWrittenQuestion(MyWrittenDeleteRequest request) {
        return myContentMapper.deleteMyWrittenQuestion(request) > 0;
    }

    @Override
    public List<MyWrittenListResponse> searchMyCommentAll(MyWrittenListRequest request) {
        return myContentMapper.selectMyCommentSearchAll(request);
    }

    @Override
    public List<MyWrittenListResponse> searchMyCommentSharing(MyWrittenListRequest request) {
        return myContentMapper.selectMyCommentSearchSharing(request);
    }

    @Override
    public List<MyWrittenListResponse> searchMyCommentQuestion(MyWrittenListRequest request) {
        return myContentMapper.selectMyCommentSearchQuestion(request);
    }
}
