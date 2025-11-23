package com.zero.plantory.domain.sharing.service;

import com.zero.plantory.domain.sharing.vo.SharingSearchVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class SharingReadServiceTest {
    @Autowired
    private SharingReadService sharingReadService;

    @Test
    @DisplayName("나눔 게시글 카드 리스트 조회")
    void getSharingListTest() {
        SharingSearchVO vo = SharingSearchVO.builder()
                .userAddress("서울")
                .keyword("")
                .limit(10)
                .offset(0)
                .build();

        log.info("조회 결과 = {}", sharingReadService.getSharingList(vo));
    }

    @Test
    @DisplayName("인기 나눔 조회")
    void getPopularSharingListTest() {
        log.info("인기 나눔 리스트 = {}", sharingReadService.getPopularSharingList());
    }

    @Test
    @DisplayName("나눔 상세 조회")
    void getSharingDetailTest() {
        log.info("나눔 상세 = {}", sharingReadService.getSharingDetail(4L));
    }

    @Test
    @DisplayName("댓글 조회")
    void getSharingCommentsTest() {
        log.info("나눔 댓글 = {}", sharingReadService.getSharingComments(12L));
    }

    @Test
    @DisplayName("메시지 상대 목록 조회")
    void getMessagePartnerTest() {
        log.info("메시지 대상 = {}", sharingReadService.getMessagePartners(1L, 12L));
    }

    @Test
    @DisplayName("내가 나눔한 내역 조회")
    void getMySharingGivenTest() {
        log.info("내가 준 나눔 내역 = {}", sharingReadService.getMySharingGiven(1L));
    }

    @Test
    @DisplayName("내가 받은 나눔 내역 조회")
    void getMySharingReceivedTest() {
        log.info("내가 받은 나눔 내역 = {}", sharingReadService.getMySharingReceived(1L));
    }
}
