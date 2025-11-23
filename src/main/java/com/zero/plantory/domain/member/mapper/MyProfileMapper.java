package com.zero.plantory.domain.member.mapper;

import com.zero.plantory.domain.member.vo.MemberInfoVO;
import com.zero.plantory.domain.member.vo.MemberUpdateRequestVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MyProfileMapper {
    MemberInfoVO selectMyInfo(Long memberId);
    int updateNoticeEnabled(@Param("memberId") Long memberId, @Param("enabled") int enabled);
    int updateMyInfo(MemberUpdateRequestVO request);
    int deleteMember(Long memberId);
}

