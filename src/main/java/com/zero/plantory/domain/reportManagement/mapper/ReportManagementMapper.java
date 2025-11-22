package com.zero.plantory.domain.reportManagement.mapper;

import com.zero.plantory.global.vo.ReportVO;
import com.zero.plantory.domain.reportManagement.vo.ReportDetailVO;
import com.zero.plantory.domain.reportManagement.vo.ReportManagementSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReportManagementMapper {
    List<ReportVO> selectReportList(ReportManagementSearchVO vo);
    int deleteReports(List<Long> ids);
    ReportDetailVO selectReportDetail(@Param("reportId") Long reportId);
    int insertAdminMemo(ReportVO vo);
    int updateStopDay(@Param("memberId") Long memberId, @Param("days") int days);
}
