package com.zero.plantory.domain.member.service;

import com.zero.plantory.domain.member.vo.MemberInfoVO;
import com.zero.plantory.domain.member.vo.MemberUpdateRequestVO;
import org.springframework.stereotype.Service;

@Service
public interface MyprofileService {
    MemberInfoVO getMyInfo(Long memberId);
    boolean updateMyInfo(MemberUpdateRequestVO request);
    boolean updateNoticeEnabled(Long memberId, int enabled);
    boolean deleteMember(Long memberId);
}
