package com.zero.plantory.domain.member.service;

import com.zero.plantory.domain.member.dto.MemberInfoResponse;
import com.zero.plantory.domain.member.dto.MemberUpdateRequest;

public interface MyprofileService {

    MemberInfoResponse getMyInfo(Long memberId);

    boolean updateNoticeEnabled(Long memberId, int enabled);

    boolean updateMyInfo(MemberUpdateRequest request);

    boolean deleteMember(Long memberId);
}
