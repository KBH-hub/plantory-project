package com.zero.plantory.DashboardMapperTest;

import com.zero.plantory.domain.dashboard.mapper.DashboardMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DashBoardMapperTest {
    @Autowired
    DashboardMapper mapper;

    @Test
    void  testSelectTodayDiaryByMemberId() {
        mapper.selectTodayDiaryByMemberId(1L)
                .forEach(System.out::println);
    }

    @Test
    void testSelectTodayWateringByMemberId() {
        mapper.selectTodayWateringByMemberId(1L)
                .forEach(System.out::println);
    }

    @Test
    void testFindRecommendedSharing() {
         mapper.selectRecommendedSharing()
                 .forEach(System.out::println);
    }

    @Test
    void countCareNeededPlants(){
        System.out.println(mapper.countCareNeededPlants(1L));
    }

    @Test
    void countTodayWatering() {
        System.out.println(mapper.countTodayWatering(1L));
    }

    @Test
    void countMyplantsByMemberId() {
        System.out.println(mapper.countMyplantsByMemberId(1L));
    }


}
