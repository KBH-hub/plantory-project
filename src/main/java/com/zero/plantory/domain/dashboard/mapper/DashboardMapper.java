package com.zero.plantory.domain.dashboard.mapper;

import com.zero.plantory.domain.dashboard.dto.RecommendedSharingDTO;
import com.zero.plantory.domain.dashboard.dto.TodayDiaryDTO;
import com.zero.plantory.domain.dashboard.dto.TodayWateringDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DashboardMapper {
    int countMyplantsByMemberId(Long memberId);
    int countTodayWatering(Long memberId);
    int countCareNeededPlants(Long memberId);
    List<RecommendedSharingDTO> selectRecommendedSharing();
    List<TodayWateringDTO> selectTodayWateringByMemberId(Long memberId);
    List<TodayDiaryDTO> selectTodayDiaryByMemberId(Long memberId);
}
