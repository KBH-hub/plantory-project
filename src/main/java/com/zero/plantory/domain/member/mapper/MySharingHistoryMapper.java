package com.zero.plantory.domain.member.mapper;

import com.zero.plantory.domain.member.dto.MySharingHistoryListRequest;
import com.zero.plantory.domain.member.dto.MySharingHistoryResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MySharingHistoryMapper {
    int countByInterestCount(@Param("memberId") Long memberId);
    int countByCompletedSharingCount(@Param("memberId") Long memberId);
    List<MySharingHistoryResponse> selectMySharingList(MySharingHistoryListRequest request);
}

