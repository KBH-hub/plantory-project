package com.zero.plantory.domain.dashboard.service;

import com.zero.plantory.domain.dashboard.dto.response.RecommendedSharingDTO;
import com.zero.plantory.domain.dashboard.dto.response.TodayDiaryDTO;
import com.zero.plantory.domain.dashboard.dto.response.TodayWateringDTO;

import java.util.List;

public interface DashboardService {
    int countMyPlants(Long memberId);
    int countTodayWatering(Long memberId);
    int countCareNeededPlants(Long memberId);

    List<RecommendedSharingDTO> getRecommendedSharingList();
    List<TodayWateringDTO> getTodayWatering(Long memberId);
    List<TodayDiaryDTO> getTodayDiary(Long memberId);
}
