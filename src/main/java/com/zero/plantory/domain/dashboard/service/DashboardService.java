package com.zero.plantory.domain.dashboard.service;

import com.zero.plantory.domain.dashboard.dto.RecommendedSharingResponseDTO;
import com.zero.plantory.domain.dashboard.dto.TodayDiaryResponseDTO;
import com.zero.plantory.domain.dashboard.dto.TodayWateringResponseDTO;

import java.util.List;

public interface DashboardService {
    int countMyPlants(Long memberId);
    int countTodayWatering(Long memberId);
    int countCareNeededPlants(Long memberId);

    List<RecommendedSharingResponseDTO> getRecommendedSharingList();
    List<TodayWateringResponseDTO> getTodayWatering(Long memberId);
    List<TodayDiaryResponseDTO> getTodayDiary(Long memberId);
}
