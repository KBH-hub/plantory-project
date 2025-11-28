package com.zero.plantory.domain.member.service;

import com.zero.plantory.domain.member.dto.MyInfoResponse;
import com.zero.plantory.domain.member.dto.MemberUpdateRequest;
import com.zero.plantory.domain.member.mapper.MyProfileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyProfileServiceImpl implements MyProfileService {

    private final MyProfileMapper myProfileMapper;

    @Override
    public MyInfoResponse getMyInfo(Long memberId) {
        MyInfoResponse myInfoResult = myProfileMapper.selectMyInfo(memberId);

        if (myInfoResult.getSharingRate() == null) {
            myInfoResult.setSharingRate(0);
        }

        return myInfoResult;
    }

    @Override
    @Transactional
    public boolean updateNoticeEnabled(Long memberId, int enabled) {
        return myProfileMapper.updateNoticeEnabled(memberId, enabled) > 0;
    }

    @Override
    @Transactional
    public boolean updateMyInfo(MemberUpdateRequest request) {
        return myProfileMapper.updateMyInfo(request) > 0;
    }

    @Override
    @Transactional
    public boolean deleteMember(Long memberId) {
        return myProfileMapper.deleteMember(memberId) > 0;
    }
}

