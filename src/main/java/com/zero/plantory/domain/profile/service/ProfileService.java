package com.zero.plantory.domain.profile.service;

import com.zero.plantory.domain.profile.dto.ProfileUpdateRequest;
import com.zero.plantory.domain.profile.dto.ProfileInfoResponse;
import com.zero.plantory.domain.profile.dto.PublicProfileResponse;

public interface ProfileService {

    ProfileInfoResponse getProfileInfo(Long memberId);

    boolean updateNoticeEnabled(Long memberId, int enabled);

    boolean updateProfileInfo(ProfileUpdateRequest request);

    boolean deleteMemberById(Long memberId);

    PublicProfileResponse getPublicProfile(Long memberId);

    boolean changePassword(Long memberId, String oldPassword, String newPassword);
}
