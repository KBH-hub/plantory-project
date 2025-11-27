package com.zero.plantory.domain.dashboard.service;

import com.zero.plantory.domain.dashboard.dto.RecommendedSharingResponse;
import com.zero.plantory.domain.dashboard.dto.TodayDiaryResponse;
import com.zero.plantory.domain.dashboard.dto.TodayWateringResponse;
import com.zero.plantory.domain.dashboard.mapper.DashboardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final DashboardMapper dashboardMapper;

    @Override
    public int countMyPlants(Long memberId) {
        return dashboardMapper.countMyplantsByMemberId(memberId);
    }

    @Override
    public int countTodayWatering(Long memberId) {
        return dashboardMapper.countTodayWatering(memberId);
    }

    @Override
    public int countCareNeededPlants(Long memberId) {
        return dashboardMapper.countCareNeededPlants(memberId);
    }

    @Override
    public List<RecommendedSharingResponse> getRecommendedSharingList() {
        return dashboardMapper.selectRecommendedSharing();
    }

    @Override
    public List<TodayWateringResponse> getTodayWatering(Long memberId) {
        return dashboardMapper.selectTodayWateringByMemberId(memberId);
    }

    @Override
    public List<TodayDiaryResponse> getTodayDiary(Long memberId) {
        return dashboardMapper.selectTodayDiaryByMemberId(memberId);
    }
}
