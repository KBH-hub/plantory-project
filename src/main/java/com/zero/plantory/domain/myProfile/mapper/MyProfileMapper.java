package com.zero.plantory.domain.myProfile.mapper;

import com.zero.plantory.domain.myProfile.dto.MyInfoResponse;
import com.zero.plantory.domain.myProfile.dto.MemberUpdateRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MyProfileMapper {
    MyInfoResponse selectMyInfo(Long memberId);
    int updateNoticeEnabled(@Param("memberId") Long memberId, @Param("enabled") int enabled);
    int updateMyInfo(MemberUpdateRequest request);
    int deleteMember(Long memberId);
}

