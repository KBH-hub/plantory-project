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

    private void assertBasic(JsonNode result) {
        assertThat(result).isNotNull();
        assertThat(result.get("body")).isNotNull();
    }

    @Test
    @DisplayName("과명 리스트 조회 테스트")
    void getClListTest() {
        JsonNode result = service.getClList();
        log.info("ClList = {}", result.toPrettyString());
        assertBasic(result);
    }

    @Test
    @DisplayName("형태 분류 조회 테스트")
    void getStleSeListTest() {
        JsonNode result = service.getStleSeList();
        log.info("stleSeList = {}", result.toPrettyString());
        assertBasic(result);
    }

    @Test
    @DisplayName("뿌리 형태 조회 테스트")
    void getRdxStleListTest() {
        JsonNode result = service.getRdxStleList();
        log.info("rootList = {}", result.toPrettyString());
        assertBasic(result);
    }

    @Test
    @DisplayName("생장 속도 조회 테스트")
    void getGrwtseVeListTest() {
        JsonNode result = service.getGrwtseVeList();
        log.info("grwtseVeList = {}", result.toPrettyString());
        assertBasic(result);
    }

    @Test
    @DisplayName("관리 난이도 조회 테스트")
    void getManageLevelListTest() {
        JsonNode result = service.getManageLevelList();
        log.info("manageLevelList = {}", result.toPrettyString());
        assertBasic(result);
    }

    @Test
    @DisplayName("관리 요구도 조회 테스트")
    void getManageDemandListTest() {
        JsonNode result = service.getManageDemandList();
        log.info("manageDemandList = {}", result.toPrettyString());
        assertBasic(result);
    }

    @Test
    @DisplayName("건조식물 목록 조회 테스트")
    void getDryGardenListTest() {
        JsonNode result = service.getDryGardenList("1", "5", "");

        log.info("dryGardenList = {}", result.toPrettyString());

        assertBasic(result);
        assertThat(result.get("body").get("items")).isNotNull();
    }

    @Test
    @DisplayName("건조식물 상세 조회 테스트")
    void getDryGardenDetailTest() {

        JsonNode list = service.getDryGardenList("1", "1", "");

        JsonNode items = list.get("body").get("items").get("item");

        String cntntsNo;

        if (items.isArray()) {
            cntntsNo = items.get(0).get("cntntsNo").asText();
        } else {
            cntntsNo = items.get("cntntsNo").asText();
        }

        JsonNode detail = service.getDryGardenDetail(cntntsNo);

        log.info("dryGardenDetail = {}", detail.toPrettyString());

        assertBasic(detail);
    }
}
