package com.zero.plantory.domain.openApi.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class DryGardenApiServiceTest {

    @Autowired
    private DryGardenApiService service;

    @Test
    @DisplayName("과명 리스트 조회 테스트")
    void getClListTest() {
        JsonNode result = service.getClList();

        log.info("과명 리스트 = {}", result.toPrettyString());
    }

    @Test
    @DisplayName("건조식물 리스트 조회 테스트")
    void getDryGardenListTest() {
        JsonNode result = service.getDryGardenList("1", "5", "");

        log.info("건조식물 리스트 = {}", result.toPrettyString());
    }

    @Test
    @DisplayName("건조식물 상세 조회 테스트")
    void getDryGardenDetailTest() {
        // cntntsNo는 DryGardenList 조회 결과 중 하나를 넣어야 정상 동작
        JsonNode list = service.getDryGardenList("1", "1", "");
        String cntntsNo = list.get("body").get("items").get("item").get(0).get("cntntsNo").asText();

        JsonNode detail = service.getDryGardenDetail(cntntsNo);

        log.info("상세 조회 = {}", detail.toPrettyString());
    }
}
