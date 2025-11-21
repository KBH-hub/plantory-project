package com.zero.plantory.domain.dashboard.mapper;

import lombok.extern.slf4j.Slf4j;
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
    void selectTodayDiaryByMemberIdTest() {
        mapper.selectTodayDiaryByMemberId(1L)
                .forEach(vo -> log.info(vo.toString()));
    }

    @Test
    void selectTodayWateringByMemberIdTest() {
        mapper.selectTodayWateringByMemberId(1L)
                .forEach(vo -> log.info(vo.toString()));
    }

    @Test
    void findRecommendedSharingTest() {
        mapper.selectRecommendedSharing()
                .forEach(vo -> log.info(vo.toString()));
    }

    @Test
    void countCareNeededPlantsTest() {
        log.info("careNeededPlants = {}", mapper.countCareNeededPlants(1L));
    }

    @Test
    void countTodayWateringTest() {
        log.info("todayWatering = {}", mapper.countTodayWatering(1L));
    }

    @Test
    void countMyplantsByMemberIdTest() {
        log.info("myplants = {}", mapper.countMyplantsByMemberId(1L));
    }

}
