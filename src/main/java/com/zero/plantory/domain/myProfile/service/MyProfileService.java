package com.zero.plantory.domain.myProfile.service;

import com.zero.plantory.domain.myProfile.dto.MemberUpdateRequest;
import com.zero.plantory.domain.myProfile.dto.MyInfoResponse;

public interface MyProfileService {

    MyInfoResponse getMyInfo(Long memberId);

    boolean updateNoticeEnabled(Long memberId, int enabled);

    boolean updateMyInfo(MemberUpdateRequest request);

    boolean deleteMember(Long memberId);
}
