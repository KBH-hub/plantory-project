package com.zero.plantory.domain.profile.mapper;

import com.zero.plantory.domain.profile.dto.MemberResponse;
import com.zero.plantory.domain.profile.dto.ProfileInfoResponse;
import com.zero.plantory.domain.profile.dto.ProfileUpdateRequest;
import com.zero.plantory.domain.profile.dto.PublicProfileResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProfileMapper {
    ProfileInfoResponse selectProfileInfo(Long memberId);
    int updateNoticeEnabled(@Param("memberId") Long memberId, @Param("enabled") int enabled);
    int updateProfileInfo(ProfileUpdateRequest request);
    int deleteMemberById(Long memberId);

    void deleteMember(Long memberId);
    PublicProfileResponse selectPublicProfile(Long memberId);
    MemberResponse selectByMemberId(@Param("memberId") Long memberId);
    int updatePassword(@Param("password") String password, @Param("memberId") Long memberId);
}

