package com.zero.plantory.domain.member.mapper;

import com.zero.plantory.domain.profile.dto.ProfileSharingHistoryListRequest;
import com.zero.plantory.domain.profile.mapper.ProfileSharingHistoryMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Slf4j
class ProfileSharingHistoryMapperTest {

    @Autowired
    ProfileSharingHistoryMapper profileSharingHistoryMapper;

    @Test
    @DisplayName("관심 나눔 카운트 조회")
    void countByInterestCountTest() {
        Long memberId = 1L;

        int count = profileSharingHistoryMapper.countByInterestCount(memberId);

        assertTrue(count >= 0, "관심 나눔 수 조회 실패");
        log.info("관심 나눔 수 = {}", count);
    }

    @Test
    @DisplayName("완료된 나눔 카운트 조회")
    void countByCompletedSharingCountTest() {
        Long memberId = 1L;

        int count = profileSharingHistoryMapper.countByCompletedSharingCount(memberId);

        assertTrue(count >= 0, "완료된 나눔 수 조회 실패");
        log.info("완료된 나눔 수 = {}", count);
    }

    @Test
    @DisplayName("나의 나눔 내역 조회")
    void selectProfileSharingHistoryList() {
        ProfileSharingHistoryListRequest request = new ProfileSharingHistoryListRequest();
        request.setMemberId(1L);
        request.setLimit(10);
        request.setOffset(0);

        var result = profileSharingHistoryMapper.selectProfileSharingList(request);
        assertNotNull(result);
    }

}
