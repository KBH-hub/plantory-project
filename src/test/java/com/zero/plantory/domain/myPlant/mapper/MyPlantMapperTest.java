package com.zero.plantory.domain.myPlant.mapper;

import com.zero.plantory.global.vo.MyPlantVO;
import lombok.extern.slf4j.Slf4j;
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
    void selectMyPlantListTest() {
        Long memberId = 20L;
        int limit = 10;
        int offset = 0;

        List<MyPlantVO> result = myPlantMapper.selectMyPlantList(memberId, limit, offset);

        log.info("result={}", result);
    }
}