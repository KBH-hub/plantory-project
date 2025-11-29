package com.zero.plantory.domain.profile.mapper;

import com.zero.plantory.domain.profile.dto.ProfileInfoResponse;
import com.zero.plantory.domain.profile.dto.MemberUpdateRequest;
import com.zero.plantory.domain.profile.dto.PublicProfileResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProfileMapper {
    ProfileInfoResponse selectProfileInfo(Long memberId);
    int updateNoticeEnabled(@Param("memberId") Long memberId, @Param("enabled") int enabled);
    int updateProfileInfo(MemberUpdateRequest request);
    int deleteMember(Long memberId);

    PublicProfileResponse selectPublicProfile(Long memberId);
}

