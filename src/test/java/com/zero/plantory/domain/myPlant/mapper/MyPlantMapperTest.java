package com.zero.plantory.domain.myPlant.mapper;

import com.zero.plantory.domain.myPlant.vo.MyPlantSearchVO;
import com.zero.plantory.global.vo.MyPlantVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class MyPlantMapperTest {

    @Autowired
    MyPlantMapper myPlantMapper;

    @Test
    @DisplayName("나의 식물 관리 화면")
    void selectMyPlantListTest() {
        Long memberId = 20L;
        int limit = 10;
        int offset = 0;

        List<MyPlantVO> result = myPlantMapper.selectMyPlantList(memberId, limit, offset);

        log.info("result={}", result);
    }

    @Test
    @DisplayName("나의 식물 이름 검색")
    void selectMyPlantByNameTest() {
        Long memberId = 2L;
        String name = "다육";

        List<MyPlantSearchVO> result = myPlantMapper.selectMyPlantByName(memberId, name);

        log.info(result.toString());
    }

    @Test
    @DisplayName("나의 식물 등록 처리")
    void insertMyPlantTest() {
        MyPlantVO vo  = MyPlantVO.builder()
                .memberId(4L)
                .name("테스트마이플랜트명")
                .type("테스트마이플랜트타입")
                .startAt(java.sql.Timestamp.valueOf("2025-10-01 00:00:00"))
                .endDate(java.sql.Timestamp.valueOf("2025-11-01 00:00:00"))
                .interval(28)
                .soil("테스트마이플랜트비료")
                .temperature("666~999℃")
                .build();

        int result = myPlantMapper.insertMyPlant(vo);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("나의 식물 수정 처리")
    void updateMyPlantTest() {
        MyPlantVO vo  = MyPlantVO.builder()
                .memberId(4L)
                .myplantId(20L)
                .name("수정11테스트마이플랜트명")
                .type("수정11테스트마이플랜트타입")
                .startAt(java.sql.Timestamp.valueOf("2025-10-01 00:00:00"))
                .endDate(java.sql.Timestamp.valueOf("2025-11-01 00:00:00"))
                .interval(29)
                .soil("테스트마이플랜트비료")
                .temperature("666~999℃")
                .build();

        int result = myPlantMapper.updateMyPlant(vo);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("나의 식물 삭제")
    void deleteMyPlantTest() {
        Long myplantId = 2L;

        int result = myPlantMapper.deletePlant(myplantId);

        log.info(String.valueOf(result));
    }
}