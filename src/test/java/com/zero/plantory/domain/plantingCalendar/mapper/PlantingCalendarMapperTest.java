package com.zero.plantory.domain.plantingCalendar.mapper;

import com.zero.plantory.domain.plantingCalendar.vo.PlantingCalendarVO;
import com.zero.plantory.domain.plantingCalendar.vo.selectMyPlantDiaryVO;
import com.zero.plantory.global.vo.DiaryVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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
        Long wateringId = 1L;

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
        Long memberId = 1L;
        LocalDateTime startDate = Timestamp.valueOf("2025-10-01 00:00:00").toLocalDateTime();
        LocalDateTime endDate   = Timestamp.valueOf("2025-11-01 00:00:00").toLocalDateTime();

        List<PlantingCalendarVO> result =
                plantingCalendarMapper.selectWateringCalendar(memberId ,startDate, endDate);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("관찰일지 월단위 조회 처리")
    void selectDiaryByMonthTest() {
        Long memberId = 1L;
        LocalDateTime startDate = Timestamp.valueOf("2025-10-01 00:00:00").toLocalDateTime();
        LocalDateTime endDate   = Timestamp.valueOf("2025-11-01 00:00:00").toLocalDateTime();

        List<PlantingCalendarVO> result =
                plantingCalendarMapper.selectDiaryCalendar(memberId, startDate, endDate);

        log.info(String.valueOf(result));
    }
    @Test
    @DisplayName("물주기 일단위 조회 처리")
    void selectWateringByDayTest() {
        Long memberId = 1L;
        LocalDateTime startDate = Timestamp.valueOf("2025-10-19 00:00:00").toLocalDateTime();
        LocalDateTime endDate   = Timestamp.valueOf("2025-10-20 00:00:00").toLocalDateTime();

        List<PlantingCalendarVO> result =
                plantingCalendarMapper.selectWateringCalendar(memberId, startDate, endDate);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("관찰일지 일단위 조회 처리")
    void selectDiaryByDayTest() {
        Long memberId = 1L;
        LocalDateTime startDate = Timestamp.valueOf("2025-10-03 00:00:00").toLocalDateTime();
        LocalDateTime endDate   = Timestamp.valueOf("2025-10-04 00:00:00").toLocalDateTime();

        List<PlantingCalendarVO> result =
                plantingCalendarMapper.selectDiaryCalendar(memberId, startDate, endDate);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("캘린더 관찰일지 수정모달 정보 조회")
    void selectDiaryUpdateInfoTest() {
        Long diary_id = 1L;

        DiaryVO result = plantingCalendarMapper.selectDiaryUpdateInfo(diary_id);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("관찰일지 수정 처리")
    void updateDiaryTest() {
        DiaryVO vo =  DiaryVO.builder()
                .diaryId(1L)
                .activity("열매 따먹기")
                .state("싫음")
                .memo("테스트 메모 내용")
                .build();

        int result = plantingCalendarMapper.updateDiary(vo);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("관찰일지 등록 - 나의 식물 선택 조회")
    void selectMyPlantTest() {
        Long memberId = 1L;

        List<selectMyPlantDiaryVO> result = plantingCalendarMapper.selectMyPlant(memberId);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("관찰일지 등록 처리")
    void insertDiaryTest() {
        DiaryVO vo = DiaryVO.builder()
                .myplantId(21L)
                .activity("열매 등록하기")
                .state("좋음")
                .memo("메모 내용")
                .build();

        int result = plantingCalendarMapper.insertDiary(vo);

        log.info(String.valueOf(result));
    }

}