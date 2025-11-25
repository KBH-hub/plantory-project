package com.zero.plantory.domain.dashboard.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class DashboardServiceTest {
    @Autowired
    DashboardService service;

    @Test
    @DisplayName("내 식물 개수 조회")
    void countMyPlantsTest() {
        log.info("내 식물 수 = {}", service.countMyPlants(1L));
    }

    @Test
    @DisplayName("오늘 물 필요 식물 수")
    void countTodayWateringTest() {
        log.info("오늘 물 필요 식물 수 = {}", service.countTodayWatering(1L));
    }

    @Test
    @DisplayName("관심 필요 식물 수")
    void countCareNeededPlantsTest() {
        log.info("관심 필요 식물 수 = {}", service.countCareNeededPlants(1L));
    }

    @Test
    @DisplayName("추천 나눔 조회")
    void getRecommendedSharingTest() {
        log.info("추천 나눔 리스트 = {}", service.getRecommendedSharingList());
    }

    @Test
    @DisplayName("오늘 물주기 일정 조회")
    void getTodayWateringTest() {
        log.info("오늘 물주기 일정 = {}", service.getTodayWatering(1L));
    }

    @Test
    @DisplayName("오늘 관찰일지 일정 조회")
    void getTodayDiaryTest() {
        log.info("오늘 관찰일지 일정 = {}", service.getTodayDiary(1L));
    }
}
