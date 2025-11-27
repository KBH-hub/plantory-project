package com.zero.plantory.domain.dashboard.controller;

import com.zero.plantory.domain.dashboard.dto.RecommendedSharingDTO;
import com.zero.plantory.domain.dashboard.dto.TodayDiaryDTO;
import com.zero.plantory.domain.dashboard.dto.TodayWateringDTO;
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

    @GetMapping("/recommended")
    public List<RecommendedSharingDTO> recommended() {
        return dashboardService.getRecommendedSharingList();
    }

    @GetMapping("/watering")
    public List<TodayWateringDTO> watering(@RequestParam Long memberId) {
        return dashboardService.getTodayWatering(memberId);
    }

    @GetMapping("/diary")
    public List<TodayDiaryDTO> diary(@RequestParam Long memberId) {
        return dashboardService.getTodayDiary(memberId);
    }
}
