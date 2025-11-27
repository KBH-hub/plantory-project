package com.zero.plantory.domain.dashboard.mapper;

import com.zero.plantory.domain.dashboard.dto.RecommendedSharingResponse;
import com.zero.plantory.domain.dashboard.dto.TodayDiaryResponse;
import com.zero.plantory.domain.dashboard.dto.TodayWateringResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DashboardMapper {
    int countMyplantsByMemberId(Long memberId);
    int countTodayWatering(Long memberId);
    int countCareNeededPlants(Long memberId);
    List<RecommendedSharingResponse> selectRecommendedSharing();
    List<TodayWateringResponse> selectTodayWateringByMemberId(Long memberId);
    List<TodayDiaryResponse> selectTodayDiaryByMemberId(Long memberId);
}
