package com.zero.plantory.domain.member.service;

import com.zero.plantory.domain.member.vo.MemberInfoVO;
import com.zero.plantory.domain.member.vo.MemberUpdateRequestVO;

public interface MyprofileService {

    MemberInfoVO getMyInfo(Long memberId);

    boolean updateNoticeEnabled(Long memberId, int enabled);

    boolean updateMyInfo(MemberUpdateRequestVO request);

    boolean deleteMember(Long memberId);
}
