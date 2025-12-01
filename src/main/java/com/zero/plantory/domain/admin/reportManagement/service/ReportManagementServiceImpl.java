package com.zero.plantory.domain.admin.reportManagement.service;

import com.zero.plantory.domain.admin.reportManagement.dto.ReportManagementSearchRequest;
import com.zero.plantory.domain.admin.reportManagement.dto.ReportResponse;
import com.zero.plantory.domain.admin.reportManagement.mapper.ReportManagementMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReportManagementServiceImpl implements ReportManagementService {

    private final ReportManagementMapper reportManagementMapper;

    @Override
    public List<ReportResponse> getReporManagmentList(ReportManagementSearchRequest request) {
        return  reportManagementMapper.selectReportList(request);
    }
}
