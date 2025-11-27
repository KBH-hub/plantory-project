package com.zero.plantory.domain.dashboard.mapper;

import com.zero.plantory.domain.dashboard.dto.response.RecommendedSharingDTO;
import com.zero.plantory.domain.dashboard.dto.response.TodayDiaryDTO;
import com.zero.plantory.domain.dashboard.dto.response.TodayWateringDTO;
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
