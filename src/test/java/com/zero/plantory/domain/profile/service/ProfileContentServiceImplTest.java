package com.zero.plantory.domain.profile.service;

import com.zero.plantory.domain.profile.dto.ProfileWrittenListRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class ProfileContentServiceImplTest {

    @Autowired
    ProfileContentService profileContentService;

    @Test
    void getProfileWrittenList_ALL() {

        ProfileWrittenListRequest req = ProfileWrittenListRequest.builder()
                .memberId(23L)
                .keyword("")
                .limit(10)
                .offset(0)
                .build();

        ProfileWrittenPageResult result =
                profileContentService.getProfileWrittenList(req, "ALL");

        assertNotNull(result, "결과가 null이면 안 됩니다.");
        assertTrue(result.getTotal() >= 0, "total은 0 이상이어야 합니다.");
        assertNotNull(result.getList(), "리스트가 null이면 안 됩니다.");

        log.info("조회 결과 total={}", result.getTotal());
        log.info("조회된 데이터: {}", result.getList());
    }
}
