package com.zero.plantory.domain.myProfile.mapper;

import com.zero.plantory.domain.myProfile.dto.MySharingHistoryListRequest;
import com.zero.plantory.domain.myProfile.dto.MySharingHistoryListResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MySharingHistoryMapper {
    int countByInterestCount(@Param("memberId") Long memberId);
    int countByCompletedSharingCount(@Param("memberId") Long memberId);
    List<MySharingHistoryListResponse> selectMySharingList(MySharingHistoryListRequest request);
}

