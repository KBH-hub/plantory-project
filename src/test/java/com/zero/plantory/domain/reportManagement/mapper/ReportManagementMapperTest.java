package com.zero.plantory.domain.reportManagement.mapper;

import com.zero.plantory.global.vo.ReportVO;
import com.zero.plantory.domain.reportManagement.vo.ReportDetailVO;
import com.zero.plantory.domain.reportManagement.vo.ReportManagementSearchVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class ReportManagementMapperTest {

    @Autowired
    ReportManagementMapper reportManagementMapper;

    @Test
    @DisplayName("신고 관리 화면 - 일반 조회")
    void selectReportListTest() {
        ReportManagementSearchVO vo = ReportManagementSearchVO.builder()
                .limit(10)
                .offset(0)
                .build();

        List<ReportVO> result = reportManagementMapper.selectReportList(vo);

        log.info("result={}", result);
    }

    @Test
    @DisplayName("신고 관리 화면 - 신고 내용으로 검색")
    void selectReportListByKeywordTest() {
        ReportManagementSearchVO vo = ReportManagementSearchVO.builder()
                .keyword("약속")
                .status(null)
                .limit(10)
                .offset(0)
                .build();

        List<ReportVO> result = reportManagementMapper.selectReportList(vo);

        log.info("result={}", result);
    }

    @Test
    @DisplayName("신고 관리 화면 - 처리 완료 필터링 조회")
    void selectReportListByStatusTest() {
        ReportManagementSearchVO vo = ReportManagementSearchVO.builder()
                .keyword(null)
                .status("true")
                .limit(10)
                .offset(0)
                .build();

        List<ReportVO> result = reportManagementMapper.selectReportList(vo);

        log.info("result={}", result);
    }

    @Test
    @DisplayName("신고 목록 삭제 처리")
    void deleteReportsTest() {
        List<Long> ids = List.of(1L, 2L);

        int result = reportManagementMapper.deleteReports(ids);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("신고 내용 상세")
    void selectReportDetailTest() {
        Long reportId = 3L;

        ReportDetailVO result = reportManagementMapper.selectReportDetail(reportId);

        log.info("result={}", result);
    }

    @Test
    @DisplayName("처리 의견 입력")
    void insertAdminMemoTest() {
        ReportVO vo = ReportVO.builder()
                .adminId(18L)
                .adminMemo("조치하였습니다")
                .status("true")
                .reportId(7L)
                .build();

        int result = reportManagementMapper.insertAdminMemo(vo);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("활동 정지 처리")
    void updateStopDayTest() {
        Long memberId = 2L;
        int days = 7;

        int result = reportManagementMapper.updateStopDay(memberId, days);

        log.info(String.valueOf(result));
    }

}