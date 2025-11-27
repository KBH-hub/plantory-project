package com.zero.plantory.domain.reportManagement.mapper;

import com.zero.plantory.domain.reportManagement.dto.ReportDetailResponse;
import com.zero.plantory.domain.reportManagement.dto.ReportManagementSearchResponse;
import com.zero.plantory.domain.reportManagement.dto.ReportResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReportManagementMapper {
    List<ReportResponse> selectReportList(ReportManagementSearchResponse vo);
    int deleteReports(List<Long> ids);
    ReportDetailResponse selectReportDetail(@Param("reportId") Long reportId);
    int insertAdminMemo(ReportResponse vo);
    int updateStopDay(@Param("memberId") Long memberId, @Param("days") int days);
}
