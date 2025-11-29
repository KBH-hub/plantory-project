package com.zero.plantory.domain.member.mapper;

import com.zero.plantory.domain.profile.dto.MemberUpdateRequest;
import com.zero.plantory.domain.profile.mapper.ProfileMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Slf4j
class ProfileMapperTest {

    @Autowired
    ProfileMapper profileMapper;

    @Test
    @DisplayName("내 프로필 조회")
    void selectProfileInfoTest() {
        var result = profileMapper.selectProfileInfo(1L);
        assertNotNull(result);
        log.info(result.toString());
    }

    @Test
    @DisplayName("알림 설정 변경")
    void updateNoticeEnabledTest() {
        int result = profileMapper.updateNoticeEnabled(1L, 1);
        assertTrue(result > 0);
    }

    @Test
    @DisplayName("내 정보 수정")
    void updateProfileInfoTest() {
        MemberUpdateRequest request = MemberUpdateRequest.builder()
                .memberId(20L)
                .nickname("홍길동22")
                .phone("010-1111-2222")
                .address("부산광역시")
                .build();

        int updated = profileMapper.updateProfileInfo(request);
        assertTrue(updated > 0);
        log.info("내 정보 수정 로그결과: "+updated);
    }

    @Test
    void deleteMemberByIdTest() {
        int result = profileMapper.deleteMemberById(1L);
        assertTrue(result > 0);
        log.info(String.valueOf(result));
    }
}
