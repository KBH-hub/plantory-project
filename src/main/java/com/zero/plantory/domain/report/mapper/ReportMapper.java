package com.zero.plantory.domain.report.mapper;

import com.zero.plantory.domain.member.vo.MemberVO;
import com.zero.plantory.domain.report.vo.ReportVO;
import com.zero.plantory.domain.report.vo.SelectNameListVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReportMapper {
    List<SelectNameListVO> selectUserIdByNickname(String nickname);
    int insertReport(ReportVO reportVO);
}
