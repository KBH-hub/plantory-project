package com.zero.plantory.domain.dashboard.mapper;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DashBoardMapperTest {
    @Autowired
    DashboardMapper mapper;

    @Test
    @DisplayName("오늘의 관찰일지 확인")
    void selectTodayDiaryByMemberIdTest() {
        mapper.selectTodayDiaryByMemberId(1L)
                .forEach(response -> log.info(response.toString()));
    }

    @Test
    @DisplayName("오늘의 급수 필요 식물 확인")
    void selectTodayWateringByMemberIdTest() {
        mapper.selectTodayWateringByMemberId(1L)
                .forEach(response -> log.info(response.toString()));
    }

    @Test
    @DisplayName("추천 나눔 글 확인")
    void selectRecommendedSharingTest() {
        mapper.selectRecommendedSharing()
                .forEach(response -> log.info(response.toString()));
    }

    @Test
    @DisplayName("관심 필요 식물 수 확인")
    void countCareNeededPlantsTest() {
        log.info("careNeededPlants = {}", mapper.countCareNeededPlants(1L));
    }

    @Test
    @DisplayName("오늘 물 필요 식물 수 확인")
    void countTodayWateringTest() {
        log.info("todayWatering = {}", mapper.countTodayWatering(1L));
    }

    @Test
    @DisplayName("내 식물 수 확인")
    void countMyplantsByMemberIdTest() {
        log.info("myplants = {}", mapper.countMyplantsByMemberId(1L));
    }

}
