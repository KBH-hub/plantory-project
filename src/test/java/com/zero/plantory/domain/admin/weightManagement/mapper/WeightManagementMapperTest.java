package com.zero.plantory.domain.admin.weightManagement.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zero.plantory.domain.admin.weightManagement.dto.SaveWeightRequest;
import com.zero.plantory.domain.admin.weightManagement.dto.WeightManagementResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
class WeightManagementMapperTest {

    @Autowired
    private WeightManagementMapper weightManagementMapper;

    @Test
    void selectWeightManagementList() throws Exception {
        List<WeightManagementResponse> list =
                weightManagementMapper.selectWeightManagementList("관", 10, 0, LocalDateTime.now());

        String json = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(list);

        log.info("결과 JSON:\n{}", json);
    }


    @Test
    void insertWeights() {
        SaveWeightRequest request = new SaveWeightRequest();
        request.setMemberId(1L);
        request.setSearchWeight(0.3);
        request.setQuestionWeight(0.7);
        int result = weightManagementMapper.insertWeights(request);
        assertEquals(1, result);
    }


    @Test
    void selectLatestWeights() {
        log.info("selectLatesWeights 로그 결과:"+weightManagementMapper.selectLatestWeights());
    }
}