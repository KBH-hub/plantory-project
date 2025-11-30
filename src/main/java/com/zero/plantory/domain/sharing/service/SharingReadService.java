package com.zero.plantory.domain.sharing.service;

import com.zero.plantory.domain.sharing.dto.*;

import java.util.List;

public interface SharingReadService {
    List<SharingCardListResponse> getSharingList(SharingSearchRequest request);

    int countInterest(Long memberId);

    List<SharingPopularResponse> getPopularSharingList();

    SelectSharingDetailResponse getSharingDetail(Long sharingId);

    List<SelectCommentListResponse> getSharingComments(Long sharingId);

    List<SharingPartnerResponse> getMessagePartners(Long receiverId, Long sharingId);

    List<SharingHistoryResponse> getMySharingGiven(Long memberId);

    List<SharingHistoryResponse> getMySharingReceived(Long memberId);

    SharingHistoryResponse getReviewInfoForGiver(Long sharingId, Long loginMemberId);

    SharingHistoryResponse getReviewInfoForReceiver(Long sharingId, Long loginMemberId);
}
