package com.zero.plantory.domain.dashboard.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DashboardSummaryResponse {

    private int myPlantsCount;
    private int todayWateringCount;
    private int careNeededCount;

    private List<RecommendedSharingResponse> recommendeds;
    private List<TodayWateringResponse> waterings;
    private List<TodayDiaryResponse> diaries;
}