package com.zero.plantory.domain.sharing.service;

import com.zero.plantory.domain.sharing.vo.SharingHistoryVO;

import java.util.List;

public interface SharingScoreService {
    /** 나눔 완료 처리 (분양자 → 피분양자 저장 + 알림 생성) */
    void completeSharing(Long sharingId, Long ownerId, Long receiverId);

    /** 리뷰 등록 */
    void registerSharingReview(Long reviewerId, Long targetMemberId, Long sharingId, int score);

    /** 점수 업데이트 */
//    void updateSharingScore(Long memberId, double score);
}
