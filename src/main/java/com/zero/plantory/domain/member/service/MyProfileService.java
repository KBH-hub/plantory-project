package com.zero.plantory.domain.member.service;

import com.zero.plantory.domain.member.dto.MyInfoResponse;
import com.zero.plantory.domain.member.dto.MemberUpdateRequest;

public interface MyProfileService {

    MyInfoResponse getMyInfo(Long memberId);

    boolean updateNoticeEnabled(Long memberId, int enabled);

    boolean updateMyInfo(MemberUpdateRequest request);

    boolean deleteMember(Long memberId);
}
