package com.zero.plantory.domain.reportManagement.mapper;

import com.zero.plantory.domain.global.vo.ReportVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReportManagementMapper {
    List<ReportVO> selectReportList(@Param("limit") int limit, @Param("offset") int offset);
}
