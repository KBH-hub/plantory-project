package com.zero.plantory.domain.report.mapper;

import com.zero.plantory.domain.report.dto.NameListResponse;
import com.zero.plantory.domain.report.dto.ReportRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
class ReportMapperTest {

    @Autowired
    ReportMapper reportMapper;

    @Test
    @DisplayName("피신고자 아이디 검색 - 닉네임으로")
    void selectUserIdByNicknameTest() {
        String nickname = "우";

        List<NameListResponse> result = reportMapper.selectUserIdByNickname(nickname);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("신고하기 등록 처리")
    void insertReportTest() {
        ReportRequest reportVO = new ReportRequest().builder()
                .reporterId(2L)
                .targetMemberId(3L)
                .content("반복 게시글 올리던데요")
                .status("false")
                .build();

        int result = reportMapper.insertReport(reportVO);

        log.info(String.valueOf(result));
    }
}