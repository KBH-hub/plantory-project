package com.zero.plantory.domain.myPlant.service;

import com.zero.plantory.domain.myPlant.dto.MyPlantRequest;
import com.zero.plantory.domain.myPlant.dto.MyPlantResponse;
import com.zero.plantory.global.dto.ImageDTO;
import com.zero.plantory.global.dto.ImageTargetType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
class MyPlantServiceTest {

    @Autowired
    MyPlantService myPlantService;

    @Test
    @DisplayName("내 식물 리스트 조회")
    void getMyPlantListTest() {
        Long memberId = 4L;
        String name = "";
        int limit = 10;
        int offset = 0;

        List<MyPlantResponse> result = myPlantService.getMyPlantList(memberId, name, limit, offset);

        log.info("result={}", result);
    }

    @Test
    @DisplayName("내 식물 리스트 이름 검색 조회")
    void getMyPlantByNameTest() {
        Long memberId = 1L;
        String name = "투";

        List<MyPlantResponse> result = myPlantService.getMyPlantByName(memberId, name);

        log.info("result={}", result);
    }

    @Test
    @DisplayName("내 식물 등록 처리")
    void registerMyPlantTest() throws IOException {
        MyPlantRequest request = MyPlantRequest.builder()
                .memberId(4L)
                .name("테스트마이플랜트명")
                .type("테스트마이플랜트타입")
                .startAt(Timestamp.valueOf("2025-10-01 00:00:00").toLocalDateTime())
                .endDate(Timestamp.valueOf("2025-11-01 00:00:00").toLocalDateTime())
                .interval(28)
                .soil("테스트마이플랜트비료")
                .temperature("666~999℃")
                .build();

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
        Long memberId = 4L;

        int result = myPlantService.registerMyPlant(request, files, memberId);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("내 식물 등록 필수값 누락 실패 처리")
    void registerMyPlantFailTest() {
        MyPlantRequest request = MyPlantRequest.builder()
                .memberId(4L)
                .name("")
                .type("테스트마이플랜트타입")
                .startAt(Timestamp.valueOf("2025-10-01 00:00:00").toLocalDateTime())
                .endDate(Timestamp.valueOf("2025-11-01 00:00:00").toLocalDateTime())
                .interval(28)
                .soil("테스트마이플랜트비료")
                .temperature("666~999℃")
                .build();

        List<MultipartFile> files = new ArrayList<>();
        MockMultipartFile file1 = new MockMultipartFile(
                "file",
                "img1.png",
                "image/png",
                "image1".getBytes()
        );

        files.add(file1);
        Long memberId = 4L;

        boolean result;
        try {
            myPlantService.registerMyPlant(request, files, memberId);
            result = true;
        } catch (Exception e) {
            result = false;
        }
        Assertions.assertFalse(result);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("내 식물 수정 처리")
    void updateMyPlantTest() throws IOException {
        MyPlantRequest request = MyPlantRequest.builder()
                .memberId(4L)
                .myplantId(26L)
                .name("수정테스트식물1")
                .type("수정테스트마이플랜트타입1")
                .startAt(Timestamp.valueOf("2025-10-01 00:00:00").toLocalDateTime())
                .endDate(Timestamp.valueOf("2025-11-01 00:00:00").toLocalDateTime())
                .interval(28)
                .soil("수정테스트마이플랜트비료1")
                .temperature("666~999℃")
                .build();

        List<Long> delImgList = new ArrayList();
        delImgList.add(26L);


        List<MultipartFile> files = new ArrayList<>();
        MockMultipartFile file1 = new MockMultipartFile(
                "file",
                "img1.png",
                "image/png",
                "image1".getBytes()
        );

        files.add(file1);
        Long memberId = 4L;

        int result = myPlantService.updateMyPlant(request, delImgList, files, memberId);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("내 식물 삭제 처리")
    void removePlantTest() {
        Long myPlantId = 1L;

        int result = myPlantService.removePlant(myPlantId);

        log.info(String.valueOf(result));
    }
}