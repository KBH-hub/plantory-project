package com.zero.plantory.domain.member.mapper;

import com.zero.plantory.domain.myProfile.dto.MemberUpdateRequest;
import com.zero.plantory.domain.myProfile.mapper.MyProfileMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Slf4j
class MyProfileMapperTest {

    @Autowired
    MyProfileMapper myProfileMapper;

    @Test
    @DisplayName("내 프로필 조회")
    void selectMyInfoTest() {
        var result = myProfileMapper.selectMyInfo(1L);
        assertNotNull(result);
        log.info(result.toString());
    }

    @Test
    @DisplayName("알림 설정 변경")
    void updateNoticeEnabledTest() {
        int result = myProfileMapper.updateNoticeEnabled(1L, 1);
        assertTrue(result > 0);
    }

    @Test
    @DisplayName("내 정보 수정")
    void updateMyInfoTest() {
        MemberUpdateRequest request = MemberUpdateRequest.builder()
                .memberId(20L)
                .nickname("홍길동22")
                .phone("010-1111-2222")
                .address("부산광역시")
                .build();

        int updated = myProfileMapper.updateMyInfo(request);
        assertTrue(updated > 0);
        log.info("내 정보 수정 로그결과: "+updated);
    }

    @Test
    void deleteMemberTest() {
        int result = myProfileMapper.deleteMember(1L);
        assertTrue(result > 0);
        log.info(String.valueOf(result));
    }
}
