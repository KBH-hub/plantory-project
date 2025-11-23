package com.zero.plantory.domain.report.mapper;

import com.zero.plantory.global.vo.ReportVO;
import com.zero.plantory.domain.report.vo.SelectNameListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReportMapper {
    List<SelectNameListVO> selectUserIdByNickname(@Param("nickname") String nickname);
    @Options(useGeneratedKeys = true, keyProperty = "reportId", keyColumn = "report_id")
    int insertReport(ReportVO reportVO);
}
