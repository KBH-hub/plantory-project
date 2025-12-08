package com.zero.plantory.domain.member.service;

import com.zero.plantory.domain.profile.dto.ProfileSharingHistoryListRequest;
import com.zero.plantory.domain.profile.dto.ProfileSharingHistoryListResponse;
import com.zero.plantory.domain.profile.service.ProfileSharingHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest
class ProfileSharingHistoryServiceImplTest {

    @Autowired
    private ProfileSharingHistoryService profileSharingHistoryService;

    @Test
    @DisplayName("나눔 관심 수 조회 테스트")
    void getInterestCountTest() {

        int result = profileSharingHistoryService.getInterestCount(1L);

        log.info("interestCount = {}", result);

        assertTrue(result >= 0);
    }

    @Test
    @DisplayName("완료된 나눔 수 조회 테스트")
    void getCompletedSharingCountTest() {

        int result = profileSharingHistoryService.getCompletedSharingCount(1L);

        log.info("completedSharingCount = {}", result);

        assertTrue(result >= 0);
    }

    @Test
    @DisplayName("나의 나눔/나눔 받은 내역 리스트 조회 테스트")
    void getProfileSharingHistoryListTest() {

        ProfileSharingHistoryListRequest req = ProfileSharingHistoryListRequest.builder()
                .memberId(1L)
                .keyword("")
                .status("")
                .offset(0)
                .limit(10)
                .build();

        List<ProfileSharingHistoryListResponse> list = profileSharingHistoryService.getMySharingList(req);
        List<ProfileSharingHistoryListResponse> list2 = profileSharingHistoryService.getReceivedSharingList(req);

        log.info("나의 나움 내역 리스트 조회 로그 결과: = {}", list);
        log.info("나눔 받은 내역 리스트 조회 로그 결과2: = {}", list2);
    }
}