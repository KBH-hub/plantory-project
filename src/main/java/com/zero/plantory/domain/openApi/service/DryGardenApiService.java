package com.zero.plantory.domain.openApi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.zero.plantory.global.utils.NongsaroApiClient;
import com.zero.plantory.global.utils.XmlToJsonConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class DryGardenApiService {

    private final NongsaroApiClient client;
    private final XmlToJsonConverter converter;

    private static final String BASE_URL = "/service/dryGarden";

    /** 과명 리스트 */
    public JsonNode getClList() {
        String xml = client.get(BASE_URL, "/clList", null);
        return converter.convert(xml);
    }

    /** 형태 분류 */
    public JsonNode getStleSeList() {
        String xml = client.get(BASE_URL, "/stleSeList", null);
        return converter.convert(xml);
    }

    /** 뿌리 형태 */
    public JsonNode getRdxStleList() {
        String xml = client.get(BASE_URL, "/rdxStleList", null);
        return converter.convert(xml);
    }

    /** 생장 속도 */
    public JsonNode getGrwtseVeList() {
        String xml = client.get(BASE_URL, "/grwtseVeList", null);
        return converter.convert(xml);
    }

    /** 난이도 */
    public JsonNode getManageLevelList() {
        String xml = client.get(BASE_URL, "/manageLevelList", null);
        return converter.convert(xml);
    }

    /** 관리 요구도 */
    public JsonNode getManageDemandList() {
        String xml = client.get(BASE_URL, "/manageDemandList", null);
        return converter.convert(xml);
    }

    /** 건조식물 목록 조회 */
    public JsonNode getDryGardenList(String pageNo, String numOfRows, String sClCode) {

        String xml = client.get(
                BASE_URL,
                "/dryGardenList",
                Map.of(
                        "pageNo", pageNo,
                        "numOfRows", numOfRows,
                        "sClCode", sClCode
                )
        );

        return converter.convert(xml);
    }

    /** 건조식물 상세 조회 */
    public JsonNode getDryGardenDetail(String cntntsNo) {

        String xml = client.get(
                BASE_URL,
                "/dryGardenDtl",
                Map.of("cntntsNo", cntntsNo)
        );

        return converter.convert(xml);
    }
}
