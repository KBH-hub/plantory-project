package com.zero.plantory.domain.member.mapper;

import com.zero.plantory.domain.member.dto.MemberInfoResponse;
import com.zero.plantory.domain.member.dto.MemberUpdateRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MyProfileMapper {
    MemberInfoResponse selectMyInfo(Long memberId);
    int updateNoticeEnabled(@Param("memberId") Long memberId, @Param("enabled") int enabled);
    int updateMyInfo(MemberUpdateRequest request);
    int deleteMember(Long memberId);
}

