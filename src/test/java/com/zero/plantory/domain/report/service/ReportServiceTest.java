package com.zero.plantory.domain.report.service;

import com.zero.plantory.domain.report.dto.NameListResponse;
import com.zero.plantory.domain.report.dto.ReportRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
class ReportServiceTest {

    @Autowired
    ReportService reportService;

    @Test
    @DisplayName("자기 자신 조회 제외")
    void getUsersIdByNicknameTest() {
        String nickname = "트";
        Long viewerId = 5L;

        List<NameListResponse> result = reportService.getUsersIdByNickname(nickname, viewerId);

        log.info("result={}", result);
    }

    @Test
    @DisplayName("신고 정상 등록")
    void registerReportTest() throws IOException {
        Long targetMemberId = 1L;
        Long reporterId = 2L;
        String content = "테스트 신고 내용";
        
        ReportRequest request = ReportRequest.builder()
                .targetMemberId(targetMemberId)
                .reporterId(reporterId)
                .content(content)
                .build();

        List<MultipartFile> files = new ArrayList<>();

        MockMultipartFile file1 = new MockMultipartFile(
                "file",
                "img1.png",
                "image/png",
                "image1".getBytes()
        );
        MockMultipartFile file2 = new MockMultipartFile(
                "file",
                "img2.png",
                "image/png",
                "image2".getBytes()
        );
        files.add(file1);
        files.add(file2);

        int result = reportService.registerReport(request, files);

        log.info(String.valueOf(result));
    }
    
    @Test
    @DisplayName("신고 필수값 누락(내용) 테스트")
    void registerReportFailTest() throws IOException {
        Long targetMemberId = 1L;
        Long reporterId = 3L;
        String content = "";

        ReportRequest request = ReportRequest.builder()
                .targetMemberId(targetMemberId)
                .reporterId(reporterId)
                .content(content)
                .build();

        List<MultipartFile> files = new ArrayList<>();

        MockMultipartFile file1 = new MockMultipartFile(
                "file",
                "img1.png",
                "image/png",
                "image1".getBytes()
        );
        files.add(file1);

        boolean result;
        try {
            reportService.registerReport(request, files);
            result = true;
        } catch (IllegalArgumentException e) {
            result = false;
        }

        Assertions.assertFalse(result);

        log.info(String.valueOf(result));
    }
}