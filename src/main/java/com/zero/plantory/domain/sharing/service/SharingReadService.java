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

    /** 내가 나눔한 내역 조회 */
    List<SharingHistoryResponse> getMySharingGiven(Long memberId);

    /** 내가 받은 나눔 내역 조회 */
    List<SharingHistoryResponse> getMySharingReceived(Long memberId);
}
