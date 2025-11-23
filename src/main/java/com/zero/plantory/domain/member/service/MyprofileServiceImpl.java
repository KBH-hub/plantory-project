package com.zero.plantory.domain.member.service;

import com.zero.plantory.domain.member.mapper.MyProfileMapper;
import com.zero.plantory.domain.member.vo.MemberInfoVO;
import com.zero.plantory.domain.member.vo.MemberUpdateRequestVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyprofileServiceImpl implements MyprofileService {

    private final MyProfileMapper myProfileMapper;

    @Override
    public MemberInfoVO getMyInfo(Long memberId) {
        return myProfileMapper.selectMyInfo(memberId);
    }

    @Override
    @Transactional
    public boolean updateNoticeEnabled(Long memberId, int enabled) {
        return myProfileMapper.updateNoticeEnabled(memberId, enabled) > 0;
    }

    @Override
    @Transactional
    public boolean updateMyInfo(MemberUpdateRequestVO request) {
        return myProfileMapper.updateMyInfo(request) > 0;
    }

    @Override
    @Transactional
    public boolean deleteMember(Long memberId) {
        return myProfileMapper.deleteMember(memberId) > 0;
    }
}

