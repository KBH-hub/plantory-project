package com.zero.plantory.domain.admin.reportManagement.mapper;

import com.zero.plantory.domain.admin.reportManagement.dto.ReportManagementDetailResponse;
import com.zero.plantory.domain.admin.reportManagement.dto.ReportManagementPageResponse;
import com.zero.plantory.domain.admin.reportManagement.dto.ReportManagementSearchRequest;
import com.zero.plantory.domain.admin.reportManagement.dto.ReportManagementResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReportManagementMapper {
    List<ReportManagementResponse> selectReportList(ReportManagementSearchRequest request);
    int selectReportTotalCount(ReportManagementSearchRequest request);
    ReportManagementDetailResponse selectReportDetail(@Param("reportId") Long reportId);
    int insertAdminMemo(ReportManagementResponse vo);
    int updateStopDay(@Param("memberId") Long memberId, @Param("days") int days);
    int deleteReports(@Param("ids") List<Long> ids);
}
