package com.zero.plantory.domain.dashboard.controller;

import com.zero.plantory.domain.dashboard.dto.DashboardSummaryResponse;
import com.zero.plantory.domain.dashboard.dto.RecommendedSharingResponse;
import com.zero.plantory.domain.dashboard.dto.TodayDiaryResponse;
import com.zero.plantory.domain.dashboard.dto.TodayWateringResponse;
import com.zero.plantory.domain.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/dashboard")
@RequiredArgsConstructor
public class DashboardRestController {
    private final DashboardService dashboardService;

    @GetMapping("/summaries")
    public DashboardSummaryResponse getDashboardSummary(@RequestParam Long memberId) {
        return DashboardSummaryResponse.builder()
                .myPlantsCount(dashboardService.countMyPlants(memberId))
                .todayWateringCount(dashboardService.countTodayWatering(memberId))
                .careNeededCount(dashboardService.countCareNeededPlants(memberId))
                .recommendeds(dashboardService.getRecommendedSharingList())
                .waterings(dashboardService.getTodayWatering(memberId))
                .diaries(dashboardService.getTodayDiary(memberId))
                .build();
    }

}
