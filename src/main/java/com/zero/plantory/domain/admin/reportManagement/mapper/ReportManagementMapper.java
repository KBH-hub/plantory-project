package com.zero.plantory.domain.admin.reportManagement.mapper;

import com.zero.plantory.domain.admin.reportManagement.dto.ReportDetailResponse;
import com.zero.plantory.domain.admin.reportManagement.dto.ReportManagementSearchRequest;
import com.zero.plantory.domain.admin.reportManagement.dto.ReportResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReportManagementMapper {
    List<ReportResponse> selectReportList(ReportManagementSearchRequest vo);
    int deleteReports(List<Long> ids);
    ReportDetailResponse selectReportDetail(@Param("reportId") Long reportId);
    int insertAdminMemo(ReportResponse vo);
    int updateStopDay(@Param("memberId") Long memberId, @Param("days") int days);
}
