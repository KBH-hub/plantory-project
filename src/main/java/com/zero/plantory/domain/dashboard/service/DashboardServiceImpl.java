package com.zero.plantory.domain.dashboard.service;

import com.zero.plantory.domain.dashboard.dto.response.RecommendedSharingDTO;
import com.zero.plantory.domain.dashboard.dto.response.TodayDiaryDTO;
import com.zero.plantory.domain.dashboard.dto.response.TodayWateringDTO;
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
    public List<RecommendedSharingDTO> getRecommendedSharingList() {
        return dashboardMapper.selectRecommendedSharing();
    }

    @Override
    public List<TodayWateringDTO> getTodayWatering(Long memberId) {
        return dashboardMapper.selectTodayWateringByMemberId(memberId);
    }

    @Override
    public List<TodayDiaryDTO> getTodayDiary(Long memberId) {
        return dashboardMapper.selectTodayDiaryByMemberId(memberId);
    }
}
