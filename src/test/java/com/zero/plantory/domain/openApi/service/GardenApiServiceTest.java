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
class GardenApiServiceTest {

    @Autowired
    private GardenApiService service;

    /** 공통: 응답 기본 구조 검사 */
    private void assertBasic(JsonNode node) {
        assertThat(node).isNotNull();
        assertThat(node.get("body")).isNotNull();
    }

    @Test
    @DisplayName("광도(light) 리스트 조회 테스트")
    void getLightListTest() {
        JsonNode result = service.getLightList();
        log.info("lightList = {}", result.toPrettyString());
        assertBasic(result);
    }

    @Test
    @DisplayName("생장 형태(grwhstle) 리스트 조회 테스트")
    void getGrwhstleListTest() {
        JsonNode result = service.getGrwhstleList();
        log.info("grwhstleList = {}", result.toPrettyString());
        assertBasic(result);
    }

    @Test
    @DisplayName("잎 색(leaf color) 리스트 조회 테스트")
    void getLefcolrListTest() {
        JsonNode result = service.getLefcolrList();
        log.info("lefcolrList = {}", result.toPrettyString());
        assertBasic(result);
    }

    @Test
    @DisplayName("잎 무늬(leaf mark) 리스트 조회 테스트")
    void getLefmrkListTest() {
        JsonNode result = service.getLefmrkList();
        log.info("lefmrkList = {}", result.toPrettyString());
        assertBasic(result);
    }

    @Test
    @DisplayName("꽃 색(flclr) 리스트 조회 테스트")
    void getFlclrListTest() {
        JsonNode result = service.getFlclrList();
        log.info("flclrList = {}", result.toPrettyString());
        assertBasic(result);
    }

    @Test
    @DisplayName("열매 색(fmldecolr) 리스트 조회 테스트")
    void getFmldecolrListTest() {
        JsonNode result = service.getFmldecolrList();
        log.info("fmldecolrList = {}", result.toPrettyString());
        assertBasic(result);
    }

    @Test
    @DisplayName("발아 계절(ignSeason) 리스트 조회 테스트")
    void getIgnSeasonListTest() {
        JsonNode result = service.getIgnSeasonList();
        log.info("ignSeasonList = {}", result.toPrettyString());
        assertBasic(result);
    }

    @Test
    @DisplayName("월동 적정온도(winterLwet) 리스트 조회 테스트")
    void getWinterLwetListTest() {
        JsonNode result = service.getWinterLwetList();
        log.info("winterLwetList = {}", result.toPrettyString());
        assertBasic(result);
    }

    @Test
    @DisplayName("가격 유형(priceType) 리스트 조회 테스트")
    void getPriceTypeListTest() {
        JsonNode result = service.getPriceTypeList();
        log.info("priceTypeList = {}", result.toPrettyString());
        assertBasic(result);
    }

    @Test
    @DisplayName("물 주기(waterCycle) 리스트 조회 테스트")
    void getWaterCycleListTest() {
        JsonNode result = service.getWaterCycleList();
        log.info("waterCycleList = {}", result.toPrettyString());
        assertBasic(result);
    }

    @Test
    @DisplayName("정원식물 목록 조회 테스트")
    void getGardenListTest() {
        JsonNode result = service.getGardenList("1", "5", "");

        log.info("gardenList = {}", result.toPrettyString());

        // 기본 구조 체크
        assertBasic(result);

        // items는 반드시 있어야 함
        assertThat(result.path("body").path("items")).isNotNull();
    }

    @Test
    @DisplayName("정원식물 상세 조회 테스트")
    void getGardenDtlTest() {

        // 1) 목록 조회
        JsonNode list = service.getGardenList("1", "5", "");
        log.info("목록조회 = {}", list.toPrettyString());

        JsonNode itemsNode = list.path("body").path("items").path("item");

        assertThat(itemsNode.isMissingNode() || itemsNode.isNull())
                .as("정원식물 목록에 item이 없습니다.")
                .isFalse();

        // 2) cntntsNo 추출 (배열/단일 둘 다 지원)
        String cntntsNo;
        if (itemsNode.isArray()) {
            cntntsNo = itemsNode.get(0).path("cntntsNo").asText();
        } else {
            cntntsNo = itemsNode.path("cntntsNo").asText();
        }

        assertThat(cntntsNo).isNotBlank();

        // 3) 상세 조회
        JsonNode detail = service.getGardenDtl(cntntsNo);

        log.info("gardenDetail = {}", detail.toPrettyString());

        assertBasic(detail);
    }

    @Test
    @DisplayName("정원식물 파일 리스트 조회 테스트")
    void getGardenFileListTest() {

        JsonNode list = service.getGardenList("1", "5", "");
        JsonNode itemsNode = list.path("body").path("items").path("item");

        assertThat(itemsNode.isMissingNode() || itemsNode.isNull())
                .as("정원식물 목록에 item이 없습니다.")
                .isFalse();

        String cntntsNo;
        if (itemsNode.isArray()) {
            cntntsNo = itemsNode.get(0).path("cntntsNo").asText();
        } else {
            cntntsNo = itemsNode.path("cntntsNo").asText();
        }

        assertThat(cntntsNo).isNotBlank();

        JsonNode files = service.getGardenFileList(cntntsNo);
        log.info("gardenFileList = {}", files.toPrettyString());

        assertBasic(files);
    }
}
