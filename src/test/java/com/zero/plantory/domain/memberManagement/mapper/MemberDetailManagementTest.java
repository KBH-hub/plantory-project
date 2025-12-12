package com.zero.plantory.domain.memberManagement.mapper;

import com.zero.plantory.domain.admin.memberManagement.dto.MemberManagementResponse;
import com.zero.plantory.domain.admin.memberManagement.mapper.MemberManagementMapper;
import com.zero.plantory.domain.profile.dto.MemberResponse;
import com.zero.plantory.global.dto.DeleteTargetType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
class MemberDetailManagementTest {
    @Autowired
    MemberManagementMapper memberManagementMapper;

    @Test
    @DisplayName("회원 관리 화면 조회")
    void selectMemberListTest() {
        String keyword = "hong";
        int limit = 10;
        int offset = 0;

        List<MemberManagementResponse> result =  memberManagementMapper.selectMemberList(keyword, limit, offset);

        log.info("result:{}", result);
    }

    @Test
    @DisplayName("부적절 글, 댓글 삭제")
    void deleteContentTest() {
        DeleteTargetType targetType = DeleteTargetType.SHARING;
        Long targetId = 1L;

        int result = memberManagementMapper.deleteContent(targetType, targetId);

        log.info(String.valueOf(result));
    }
}