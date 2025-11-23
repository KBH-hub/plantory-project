package com.zero.plantory.domain.sharing.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class SharingScoreServiceTest {
    @Autowired
    private SharingScoreService sharingScoreService;

    @Test
    @DisplayName("분양 완료 처리 + 알림 전송")
    void completeSharingTest() {

        Long sharingId = 10L;
        Long memberId   = 11L;
        Long targetMemberId = 2L;

        sharingScoreService.completeSharing(sharingId, memberId, targetMemberId);

        log.info("분양 완료 처리 및 알림 전송 성공 → sharingId={}, receiverId={}", sharingId, targetMemberId);
    }
}
