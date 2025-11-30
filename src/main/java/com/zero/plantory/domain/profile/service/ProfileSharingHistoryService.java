package com.zero.plantory.domain.profile.service;

import com.zero.plantory.domain.profile.dto.ProfileSharingHistoryListRequest;
import com.zero.plantory.domain.profile.dto.ProfileSharingHistoryListResponse;
import com.zero.plantory.domain.profile.dto.ProfileSharingHistoryPageResponse;

import java.util.List;

public interface ProfileSharingHistoryService {

    int getInterestCount(Long memberId);

    int getCompletedSharingCount(Long memberId);

    ProfileSharingHistoryPageResponse getProfileSharingHistoryList(ProfileSharingHistoryListRequest req);

}
