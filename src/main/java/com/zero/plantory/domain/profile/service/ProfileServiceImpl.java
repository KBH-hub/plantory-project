package com.zero.plantory.domain.profile.service;

import com.zero.plantory.domain.member.mapper.MemberMapper;
import com.zero.plantory.domain.profile.dto.MemberResponse;
import com.zero.plantory.domain.profile.dto.ProfileInfoResponse;
import com.zero.plantory.domain.profile.dto.MemberUpdateRequest;
import com.zero.plantory.domain.profile.dto.PublicProfileResponse;
import com.zero.plantory.domain.profile.mapper.ProfileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileMapper profileMapper;
    private final MemberMapper memberMapper;
    private final BCryptPasswordEncoder  bCryptPasswordEncoder;

    @Override
    public ProfileInfoResponse getProfileInfo(Long memberId) {
        ProfileInfoResponse myInfoResult = profileMapper.selectProfileInfo(memberId);

        if (myInfoResult.getSharingRate() == null) {
            myInfoResult.setSharingRate(0);
        }

        return myInfoResult;
    }

    @Override
    @Transactional
    public boolean updateNoticeEnabled(Long memberId, int enabled) {
        return profileMapper.updateNoticeEnabled(memberId, enabled) > 0;
    }

    @Override
    @Transactional
    public boolean updateMyInfo(MemberUpdateRequest request) {
        return profileMapper.updateProfileInfo(request) > 0;
    }

    @Override
    @Transactional
    public boolean deleteMember(Long memberId) {
        return profileMapper.deleteMember(memberId) > 0;
    }

    @Override
    public PublicProfileResponse getPublicProfile(Long memberId) {
        return profileMapper.selectPublicProfile(memberId);
    }

    @Transactional
    @Override
    public boolean changePassword(Long memberId, String oldPassword, String newPassword) {
        MemberResponse member = memberMapper.selectByMemberId(memberId);

        if (!bCryptPasswordEncoder.matches(oldPassword, member.getPassword())) {
            return false;
        }

        member.setPassword(bCryptPasswordEncoder.encode(newPassword));
        memberMapper.updatePassword(member.getPassword(),member.getMemberId());

        return true;
    }

}

