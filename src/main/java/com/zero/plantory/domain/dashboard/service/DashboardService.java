package com.zero.plantory.domain.dashboard.service;

import com.zero.plantory.domain.dashboard.dto.RecommendedSharingResponse;
import com.zero.plantory.domain.dashboard.dto.TodayDiaryResponse;
import com.zero.plantory.domain.dashboard.dto.TodayWateringResponse;

import java.util.List;

public interface DashboardService {
    int countMyPlants(Long memberId);
    int countTodayWatering(Long memberId);
    int countCareNeededPlants(Long memberId);

    List<RecommendedSharingResponse> getRecommendedSharingList();
    List<TodayWateringResponse> getTodayWatering(Long memberId);
    List<TodayDiaryResponse> getTodayDiary(Long memberId);
}
