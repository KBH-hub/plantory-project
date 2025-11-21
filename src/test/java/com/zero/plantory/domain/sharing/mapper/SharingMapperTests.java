package com.zero.plantory.domain.sharing.mapper;

import com.zero.plantory.domain.sharing.vo.SharingSearchVO;
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
public class SharingMapperTests {

    @Autowired
    SharingMapper mapper;

    @Test
    @DisplayName("내 관심 나눔 식물 수 조회")
    void countInterestByMemberIdTest(){
        log.info("interestPlants = {}", mapper.countInterestByMemberId(1L));
    }

    @Test
    @DisplayName("나눔식물 조회 및 검색")
    void selectSharingListByAddressAndKeywordTest() {
        SharingSearchVO vo = SharingSearchVO.builder()
                .userAddress("서울특별시 금천구")
                .keyword("")   // 검색어 없음 → 전체조회
                .limit(10)
                .offset(0)
                .build();

        mapper.selectSharingListByAddressAndKeyword(vo)
                .forEach(item -> log.info(item.toString()));

    }



}
