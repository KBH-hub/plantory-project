package com.zero.plantory.domain.plantingCalendar.service;

import com.zero.plantory.domain.plantingCalendar.dto.SMSRequestDTO;
import com.zero.plantory.domain.plantingCalendar.vo.PlantingCalendarVO;
import com.zero.plantory.domain.plantingCalendar.vo.selectMyPlantDiaryVO;
import com.zero.plantory.global.vo.DiaryVO;
import com.zero.plantory.global.vo.ImageTargetType;
import com.zero.plantory.global.vo.ImageVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
@Slf4j
class PlantingCalenderServiceTest {
    @Autowired
    PlantingCalenderService plantCalenderService;

    @Autowired
    SMSService smsService;


    @Test
    @DisplayName("물주기 알림 전송 처리")
    void deleteDiaryTest() throws Exception {
        SMSRequestDTO dto = SMSRequestDTO.builder()
                .to("01000000000")
                .from("01000000000")
                .text("문자 전송 테스트")
                .build();
        log.info(smsService.sendSMS(dto).toString());
    }

    @Test
    @DisplayName("물주기 체크")
    void updatePlantWateringCheckTest() {
        Long myplantId = 1L;

        int result = plantCalenderService.updatePlantWateringCheck(myplantId);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("물주기 삭제")
    void removePlantWateringTest() {
        Long myplantId = 1L;
        Long removerId = 4L;

        int result = plantCalenderService.removePlantWatering(myplantId, removerId);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("캘린더 물주기 월간 조회")
    void getWateringCalendarTest() {
        Long memberId = 1L;
        LocalDateTime startDate = Timestamp.valueOf("2025-10-01 00:00:00").toLocalDateTime();
        LocalDateTime endDate   = Timestamp.valueOf("2025-11-01 00:00:00").toLocalDateTime();

        List<PlantingCalendarVO> result =
                plantCalenderService.getWateringCalendar(memberId, startDate, endDate);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("캘린더 관찰일지 월간 조회")
    void getDiaryCalendarTest() {
        Long memberId = 1L;
        LocalDateTime startDate = Timestamp.valueOf("2025-10-01 00:00:00").toLocalDateTime();
        LocalDateTime endDate   = Timestamp.valueOf("2025-11-01 00:00:00").toLocalDateTime();

        List<PlantingCalendarVO> result =
                plantCalenderService.getDiaryCalendar(memberId, startDate, endDate);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("캘린더 관찰일지 수정 모달 정보 조회")
    void findDiaryUpdateInfoTest() {
        Long diaryId = 2L;

        DiaryVO result = plantCalenderService.findDiaryUpdateInfo(diaryId);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("캘린더 관찰일지 수정 모달 이미지 조회")
    void findDiaryUpdateImageInfoTest() {
        Long diaryId = 30L;

        List<ImageVO> result = plantCalenderService.findDiaryUpdateImageInfo(diaryId);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("캘린더 관찰일지 수정 모달 수정 처리")
    void updateDiaryTest() throws IOException {
        Long memberId = 2L;
        //수정할 다이어리 정보
        DiaryVO diaryVO = DiaryVO.builder()
                .diaryId(3L)
                .activity("열매먹기")
                .state("허전함")
                .memo("맛있었음")
                .build();

        //삭제할 이미지 정보
        List<ImageVO> delImgList = new ArrayList();
        delImgList.add(ImageVO.builder()
                        .memberId(2L)
                        .targetType(ImageTargetType.DIARY)
                        .targetId(30L)
                        .imageId(30L)
                .build());
        delImgList.add(ImageVO.builder()
                        .memberId(2L)
                        .targetType(ImageTargetType.DIARY)
                        .targetId(30L)
                        .imageId(31L)
                .build());

        // 추가할 이미지 정보
        List<MultipartFile> files = new ArrayList<>();

        MockMultipartFile file1 = new MockMultipartFile(
                "file",
                "img1.png",
                "image/png",
                "image1".getBytes()
        );
        MockMultipartFile file2 = new MockMultipartFile(
                "file",
                "img2.png",
                "image/png",
                "image2".getBytes()
        );

        files.add(file1);
        files.add(file2);

        int result = plantCalenderService.updateDiary(diaryVO, delImgList, files, memberId);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("관찰 일지 등록 - 나의 식물 조회")
    void findMyPlantTest() {
        Long memberId = 2L;

        List<selectMyPlantDiaryVO> result =  plantCalenderService.getMyPlant(memberId);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("관찰일지 등록 처리")
    void registerDiaryTest() throws IOException {
        Long memberId = 2L;
        DiaryVO diaryVO = DiaryVO.builder()
                .myplantId(3L)
                .activity("열매먹기")
                .state("허전함")
                .memo("맛있었음")
                .build();

        //등록할 사진 정보
        List<MultipartFile> files = new ArrayList<>();

        MockMultipartFile file1 = new MockMultipartFile(
                "file",
                "img1.png",
                "image/png",
                "image1".getBytes()
        );
        MockMultipartFile file2 = new MockMultipartFile(
                "file",
                "img2.png",
                "image/png",
                "image2".getBytes()
        );

        files.add(file1);
        files.add(file2);

        plantCalenderService.registerDiary(diaryVO, files, memberId);
    }

}