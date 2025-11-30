package com.zero.plantory.domain.profile.service;

import com.zero.plantory.domain.profile.dto.ProfileWrittenDeleteRequest;
import com.zero.plantory.domain.profile.dto.ProfileWrittenListRequest;
import com.zero.plantory.domain.profile.dto.ProfileWrittenListResponse;

import java.util.List;

public interface ProfileContentService {

    List<ProfileWrittenListResponse> getProfileWrittenListAll(ProfileWrittenListRequest request);
    List<ProfileWrittenListResponse> getProfileWrittenListSharing(ProfileWrittenListRequest request);
    List<ProfileWrittenListResponse> getProfileWrittenListQuestion(ProfileWrittenListRequest request);

    boolean deleteProfileWrittenSharing(ProfileWrittenDeleteRequest request);
    boolean deleteProfileWrittenQuestion(ProfileWrittenDeleteRequest request);

    List<ProfileWrittenListResponse> searchProfileCommentAll(ProfileWrittenListRequest request);
    List<ProfileWrittenListResponse> searchProfileCommentSharing(ProfileWrittenListRequest request);
    List<ProfileWrittenListResponse> searchProfileCommentQuestion(ProfileWrittenListRequest request);

    ProfileWrittenPageResult getProfileWrittenList(ProfileWrittenListRequest request, String category);
}
