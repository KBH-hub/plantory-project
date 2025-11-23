package com.zero.plantory.domain.member.service;

import com.zero.plantory.domain.member.mapper.MyContentMapper;
import com.zero.plantory.domain.member.vo.MyWrittenDeleteRequestVO;
import com.zero.plantory.domain.member.vo.MyWrittenListRequestVO;
import com.zero.plantory.domain.member.vo.MyWrittenListVO;
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
    public List<MyWrittenListVO> getMyWrittenListAll(MyWrittenListRequestVO request) {
        return myContentMapper.selectMyWrittenListAll(request);
    }

    @Override
    public List<MyWrittenListVO> getMyWrittenListSharing(MyWrittenListRequestVO request) {
        return myContentMapper.selectMyWrittenListSharing(request);
    }

    @Override
    public List<MyWrittenListVO> getMyWrittenListQuestion(MyWrittenListRequestVO request) {
        return myContentMapper.selectMyWrittenListQuestion(request);
    }

    @Override
    @Transactional
    public boolean deleteMyWrittenSharing(MyWrittenDeleteRequestVO request) {
        return myContentMapper.deleteMyWrittenSharing(request) > 0;
    }

    @Override
    @Transactional
    public boolean deleteMyWrittenQuestion(MyWrittenDeleteRequestVO request) {
        return myContentMapper.deleteMyWrittenQuestion(request) > 0;
    }

    @Override
    public List<MyWrittenListVO> searchMyCommentAll(MyWrittenListRequestVO request) {
        return myContentMapper.selectMyCommentSearchAll(request);
    }

    @Override
    public List<MyWrittenListVO> searchMyCommentSharing(MyWrittenListRequestVO request) {
        return myContentMapper.selectMyCommentSearchSharing(request);
    }

    @Override
    public List<MyWrittenListVO> searchMyCommentQuestion(MyWrittenListRequestVO request) {
        return myContentMapper.selectMyCommentSearchQuestion(request);
    }
}
