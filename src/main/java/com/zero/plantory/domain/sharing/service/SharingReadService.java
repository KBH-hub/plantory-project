package com.zero.plantory.domain.sharing.service;

import com.zero.plantory.domain.sharing.dto.*;

import java.util.List;

public interface SharingReadService {
    /** 나눔 게시글 메인 리스트 조회 */
    List<SharingCardListResponse> getSharingList(SharingSearchRequest request);

    /** 인기 나눔글 조회 */
    List<SharingPopularResponse> getPopularSharingList();

    /** 나눔글 상세 조회 */
    SelectSharingDetailResponse getSharingDetail(Long sharingId);

    /** 상세 댓글 조회 */
    List<SelectCommentListResponse> getSharingComments(Long sharingId);

    /** 메시지 상대 목록 조회 */
    List<SharingPartnerResponse> getMessagePartners(Long receiverId, Long sharingId);

    /** 내가 나눔한 내역 조회 */
    List<SharingHistoryResponse> getMySharingGiven(Long memberId);

    /** 내가 받은 나눔 내역 조회 */
    List<SharingHistoryResponse> getMySharingReceived(Long memberId);
}
