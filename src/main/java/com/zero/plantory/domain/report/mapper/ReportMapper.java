package com.zero.plantory.domain.report.mapper;

import com.zero.plantory.domain.report.dto.NameListResponse;
import com.zero.plantory.domain.report.dto.ReportRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReportMapper {
    List<NameListResponse> selectUserIdByNickname(@Param("nickname") String nickname);
    @Options(useGeneratedKeys = true, keyProperty = "reportId", keyColumn = "report_id")
    int insertReport(ReportRequest request);
}
