package com.zero.plantory.domain.admin.reportManagement.service;

import com.zero.plantory.domain.admin.reportManagement.dto.ReportManagementPageResponse;
import com.zero.plantory.domain.admin.reportManagement.dto.ReportManagementSearchRequest;

import java.util.List;


public interface ReportManagementService {
    ReportManagementPageResponse getReporManagmentList(ReportManagementSearchRequest request);
    int deleteReporManagmentList(List<Long> ids);
}
