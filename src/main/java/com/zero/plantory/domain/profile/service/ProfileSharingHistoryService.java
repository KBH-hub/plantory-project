package com.zero.plantory.domain.profile.service;

import com.zero.plantory.domain.profile.dto.ProfileSharingHistoryListRequest;
import com.zero.plantory.domain.profile.dto.ProfileSharingHistoryListResponse;

import java.util.List;

public interface ProfileSharingHistoryService {

    int getInterestCount(Long memberId);

    int getCompletedSharingCount(Long memberId);

List<ProfileSharingHistoryListResponse> getMySharingList(ProfileSharingHistoryListRequest request);
List<ProfileSharingHistoryListResponse> getReceivedSharingList(ProfileSharingHistoryListRequest request);
}