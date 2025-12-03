package com.zero.plantory.domain.admin.reportManagement.service;

import com.zero.plantory.domain.admin.reportManagement.dto.*;
import com.zero.plantory.domain.admin.reportManagement.mapper.ReportManagementMapper;
import com.zero.plantory.domain.member.mapper.MemberMapper;
import com.zero.plantory.domain.profile.dto.MemberResponse;
import com.zero.plantory.domain.profile.mapper.ProfileMapper;
import com.zero.plantory.domain.profile.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReportManagementServiceImpl implements ReportManagementService {

    private final ReportManagementMapper reportManagementMapper;
    private final AdminUserService adminUserService;
    private final ProfileMapper profileMapper;

    @Override
    public ReportManagementPageResponse getReporManagmentList(ReportManagementSearchRequest request) {

        int totalCount = reportManagementMapper.selectReportTotalCount(request);
        List<ReportManagementResponse> list = reportManagementMapper.selectReportList(request);

        return ReportManagementPageResponse.builder()
                .totalCount(totalCount)
                .list(list)
                .build();
    }

    @Override
    public int deleteReporManagmentList(List<Long> ids) {
        return reportManagementMapper.deleteReports(ids);
    }

    @Override
    public ReportManagementDetailResponse getReportDetail(Long reportId) {
        return reportManagementMapper.selectReportDetail(reportId);
    }

        @Transactional
        @Override
        public void processReport(Long reportId,
                                  Long memberId,
                                  String adminMemo,
                                  int stopDays) {

            ReportManagementResponse memo = new ReportManagementResponse();
            memo.setReportId(reportId);
            memo.setAdminMemo(adminMemo);
            memo.setStatus("true");

            reportManagementMapper.insertAdminMemo(memo);

            reportManagementMapper.updateStopDay(memberId, stopDays);
            MemberResponse memberResponse = profileMapper.selectByMemberId(memberId);
            adminUserService.forceLogout(memberResponse.getMembername());
        }

}
