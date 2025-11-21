package com.zero.plantory.domain.plantingCalendar.mapper;

import com.zero.plantory.domain.plantingCalendar.vo.PlantingCalendarVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
@Slf4j
class PlantingCalendarMapperTest {
    @Autowired
    PlantingCalendarMapper plantingCalendarMapper;

    @Test
    @DisplayName("물주기 등록 처리")
    void insertWateringTest() {
        Long myplantId = 2L;

        int result = plantingCalendarMapper.insertWatering(myplantId);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("물주기 체크박스 수정 처리")
    void updatePlantWateringCheckTest() {
        Long wateringId = 25L;

        int result = plantingCalendarMapper.updatePlantWateringCheck(wateringId);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("물주기 삭제 처리 - 내 식물 정보에서 물주기 정보 null 처리")
    void updateMyPlantWateringTest() {
        Long myplantId = 2L;

        int result = plantingCalendarMapper.updateMyPlantWatering(myplantId);

        log.info(String.valueOf(result));

    }

    @Test
    @DisplayName("물주기 삭제 처리 - 물주기 정보 삭제")
    void deletePlantWateringTest() {
        Long myplantId = 2L;

        int result = plantingCalendarMapper.deletePlantWatering(myplantId);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("물주기 월단위 조회 처리")
    void selectWateringByMonthTest() {
        Date startDate = java.sql.Timestamp.valueOf("2025-10-01 00:00:00");
        Date endDate   = java.sql.Timestamp.valueOf("2025-11-01 00:00:00");

        List<PlantingCalendarVO> result =
                plantingCalendarMapper.selectWateringCalendar(startDate, endDate);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("관찰일지 월단위 조회 처리")
    void selectDiaryByMonthTest() {
        Date startDate = java.sql.Timestamp.valueOf("2025-10-01 00:00:00");
        Date endDate   = java.sql.Timestamp.valueOf("2025-11-01 00:00:00");

        List<PlantingCalendarVO> result =
                plantingCalendarMapper.selectDiaryCalendar(startDate, endDate);

        log.info(String.valueOf(result));
    }
    @Test
    @DisplayName("물주기 일단위 조회 처리")
    void selectWateringByDayTest() {
        Date startDate = java.sql.Timestamp.valueOf("2025-10-19 00:00:00");
        Date endDate   = java.sql.Timestamp.valueOf("2025-10-20 00:00:00");

        List<PlantingCalendarVO> result =
                plantingCalendarMapper.selectWateringCalendar(startDate, endDate);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("관찰일지 일단위 조회 처리")
    void selectDiaryByDayTest() {
        Date startDate = java.sql.Timestamp.valueOf("2025-10-19 00:00:00");
        Date endDate   = java.sql.Timestamp.valueOf("2025-10-20 00:00:00");

        List<PlantingCalendarVO> result =
                plantingCalendarMapper.selectDiaryCalendar(startDate, endDate);

        log.info(String.valueOf(result));
    }


}