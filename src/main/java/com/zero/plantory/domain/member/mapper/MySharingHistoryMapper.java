package com.zero.plantory.domain.member.mapper;

import com.zero.plantory.domain.member.vo.MySharingHistoryListRequestVO;
import com.zero.plantory.domain.member.vo.MySharingHistoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MySharingHistoryMapper {
    int countByInterestCount(@Param("memberId") Long memberId);
    int countByCompletedSharingCount(@Param("memberId") Long memberId);
    List<MySharingHistoryVO> selectMySharingList(MySharingHistoryListRequestVO request);
}

