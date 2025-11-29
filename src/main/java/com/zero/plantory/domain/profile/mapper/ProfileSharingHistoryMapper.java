package com.zero.plantory.domain.profile.mapper;

import com.zero.plantory.domain.profile.dto.ProfileSharingHistoryListRequest;
import com.zero.plantory.domain.profile.dto.ProfileSharingHistoryListResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProfileSharingHistoryMapper {
    int countByInterestCount(@Param("memberId") Long memberId);
    int countByCompletedSharingCount(@Param("memberId") Long memberId);
    List<ProfileSharingHistoryListResponse> selectProfileSharingList(ProfileSharingHistoryListRequest request);
}

