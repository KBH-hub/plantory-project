package com.zero.plantory.domain.dashboard.service;

import com.zero.plantory.domain.dashboard.mapper.DashboardMapper;
import com.zero.plantory.domain.dashboard.vo.RecommendedSharingVO;
import com.zero.plantory.domain.dashboard.vo.TodayDiaryVO;
import com.zero.plantory.domain.dashboard.vo.TodayWateringVO;
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
    public List<RecommendedSharingVO> getRecommendedSharingList() {
        // 추천 로직

        return dashboardMapper.selectRecommendedSharing();
    }

    @Override
    public List<TodayWateringVO> getTodayWatering(Long memberId) {
        return dashboardMapper.selectTodayWateringByMemberId(memberId);
    }

    @Override
    public List<TodayDiaryVO> getTodayDiary(Long memberId) {
        return dashboardMapper.selectTodayDiaryByMemberId(memberId);
    }


}
