package com.zero.plantory.domain.admin.reportManagement.service;

import com.zero.plantory.domain.admin.reportManagement.dto.ReportManagementDetailResponse;
import com.zero.plantory.domain.admin.reportManagement.dto.ReportManagementPageResponse;
import com.zero.plantory.domain.admin.reportManagement.dto.ReportManagementSearchRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ReportManagementService {
    ReportManagementPageResponse getReporManagmentList(ReportManagementSearchRequest request);
    int deleteReporManagmentList(List<Long> ids);
    ReportManagementDetailResponse getReportDetail(Long reportId);
    void processReport(Long reportId, Long memberId, String adminMemo, int stopDays);

}
