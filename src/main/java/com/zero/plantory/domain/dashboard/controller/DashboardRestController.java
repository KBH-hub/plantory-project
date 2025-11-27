package com.zero.plantory.domain.dashboard.controller;

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

    @GetMapping("/countMyplants")
    public int countMyPlants(@RequestParam Long memberId) {
        return dashboardService.countMyPlants(memberId);
    }

    @GetMapping("/countWatering")
    public int countTodayWatering(@RequestParam Long memberId) {
        return dashboardService.countTodayWatering(memberId);
    }

    @GetMapping("/countCareneeded")
    public int countCareNeeded(@RequestParam Long memberId) {
        return dashboardService.countCareNeededPlants(memberId);
    }

    @GetMapping("/recommendeds")
    public List<RecommendedSharingResponse> getRecommendeds() {
        return dashboardService.getRecommendedSharingList();
    }

    @GetMapping("/waterings")
    public List<TodayWateringResponse> getWaterings(@RequestParam Long memberId) {
        return dashboardService.getTodayWatering(memberId);
    }

    @GetMapping("/diaries")
    public List<TodayDiaryResponse> getDiaries(@RequestParam Long memberId) {
        return dashboardService.getTodayDiary(memberId);
    }
}
