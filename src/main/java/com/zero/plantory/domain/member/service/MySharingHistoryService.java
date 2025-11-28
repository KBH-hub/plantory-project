package com.zero.plantory.domain.member.service;

import com.zero.plantory.domain.member.dto.MySharingHistoryListRequest;
import com.zero.plantory.domain.member.dto.MySharingHistoryListResponse;

import java.util.List;

public interface MySharingHistoryService {

    int getInterestCount(Long memberId);

    int getCompletedSharingCount(Long memberId);

    List<MySharingHistoryListResponse> getMySharingHistoryList(MySharingHistoryListRequest request);
}
