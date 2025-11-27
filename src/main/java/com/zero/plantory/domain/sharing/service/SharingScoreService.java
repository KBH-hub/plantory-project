package com.zero.plantory.domain.sharing.service;

public interface SharingScoreService {
    /** 나눔 완료 처리 (분양자 → 피분양자 저장 + 알림 생성) */
    void completeSharing(Long sharingId, Long memberId, Long targetMemberId);

    /** 리뷰 등록 및 나눔지수 계산 후 등록 */
    void registerSharingReview(Long sharingId,
                               Long loginUserId,
                               int manner,
                               int reShare,
                               Integer satisfaction);


}
