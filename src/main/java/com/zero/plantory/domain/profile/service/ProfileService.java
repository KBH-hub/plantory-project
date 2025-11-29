package com.zero.plantory.domain.profile.service;

import com.zero.plantory.domain.profile.dto.MemberUpdateRequest;
import com.zero.plantory.domain.profile.dto.ProfileInfoResponse;
import com.zero.plantory.domain.profile.dto.PublicProfileResponse;
import org.springframework.transaction.annotation.Transactional;

public interface ProfileService {

    ProfileInfoResponse getProfileInfo(Long memberId);

    boolean updateNoticeEnabled(Long memberId, int enabled);

    boolean updateMyInfo(MemberUpdateRequest request);

    boolean deleteMember(Long memberId);

    PublicProfileResponse getPublicProfile(Long memberId);

    @Transactional
    boolean changePassword(Long memberId, String oldPw, String newPw);
}
