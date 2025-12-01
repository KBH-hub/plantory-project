package com.zero.plantory.domain.sharing.service;

public interface SharingScoreService {
    void completeSharing(Long sharingId, Long memberId, Long targetMemberId);

    void registerSharingReview(Long sharingId,
                               Long loginUserId,
                               int manner,
                               int reShare,
                               Integer satisfaction);


}
