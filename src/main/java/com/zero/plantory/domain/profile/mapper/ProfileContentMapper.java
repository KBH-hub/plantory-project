package com.zero.plantory.domain.profile.mapper;

import com.zero.plantory.domain.profile.dto.ProfileWrittenDeleteRequest;
import com.zero.plantory.domain.profile.dto.ProfileWrittenListRequest;
import com.zero.plantory.domain.profile.dto.ProfileWrittenListResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProfileContentMapper {
    List<ProfileWrittenListResponse> selectProfileWrittenListAll(ProfileWrittenListRequest request);

    List<ProfileWrittenListResponse> selectProfileWrittenListSharing(ProfileWrittenListRequest request);

    List<ProfileWrittenListResponse> selectProfileWrittenListQuestion(ProfileWrittenListRequest request);

    int countProfileWrittenAll(ProfileWrittenListRequest request);

    int countProfileCommentAll(ProfileWrittenListRequest request);

    int deleteProfileWrittenSharing(ProfileWrittenDeleteRequest request);

    int deleteProfileWrittenQuestion(ProfileWrittenDeleteRequest request);

    List<ProfileWrittenListResponse> selectProfileCommentSearchAll(ProfileWrittenListRequest request);

    List<ProfileWrittenListResponse> selectProfileCommentSearchSharing(ProfileWrittenListRequest request);

    List<ProfileWrittenListResponse> selectProfileCommentSearchQuestion(ProfileWrittenListRequest request);

    int countProfileWrittenQuestion(ProfileWrittenListRequest req);

    int countProfileWrittenSharing(ProfileWrittenListRequest req);

    int countProfileCommentSharing(ProfileWrittenListRequest req);

    int countProfileCommentQuestion(ProfileWrittenListRequest req);
}
