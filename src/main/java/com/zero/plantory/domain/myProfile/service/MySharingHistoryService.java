package com.zero.plantory.domain.myProfile.service;

import com.zero.plantory.domain.myProfile.dto.MySharingHistoryListRequest;
import com.zero.plantory.domain.myProfile.dto.MySharingHistoryListResponse;

import java.util.List;

public interface MySharingHistoryService {

    int getInterestCount(Long memberId);

    int getCompletedSharingCount(Long memberId);

    List<MySharingHistoryListResponse> getMySharingHistoryList(MySharingHistoryListRequest request);
}
