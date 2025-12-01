package com.zero.plantory.domain.admin.reportManagement.service;

import com.zero.plantory.domain.admin.reportManagement.dto.ReportManagementSearchRequest;
import com.zero.plantory.domain.admin.reportManagement.dto.ReportResponse;

import java.util.List;

public interface ReportManagementService {
    List<ReportResponse> getReporManagmentList(ReportManagementSearchRequest request);
}
