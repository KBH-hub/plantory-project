package com.zero.plantory.domain.reportManagement.mapper;

import com.zero.plantory.domain.global.vo.ReportVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class ReportManagementMapperTest {

    @Autowired
    ReportManagementMapper reportManagementMapper;

    @Test
    @DisplayName("신고 관리 화면")
    void selectReportList() {
        List<ReportVO> result = reportManagementMapper.selectReportList(10,0);

        log.info("result={}", result);
    }
}