package com.zero.plantory.domain.dashboard.service;

import com.zero.plantory.domain.dashboard.vo.RecommendedSharingVO;
import com.zero.plantory.domain.dashboard.vo.TodayDiaryVO;
import com.zero.plantory.domain.dashboard.vo.TodayWateringVO;

import java.util.List;

public interface DashboardService {
    int countMyPlants(Long memberId);
    int countTodayWatering(Long memberId);
    int countCareNeededPlants(Long memberId);

    List<RecommendedSharingVO> getRecommendedSharingList();

    List<TodayWateringVO> getTodayWatering(Long memberId);
    List<TodayDiaryVO> getTodayDiary(Long memberId);
}
