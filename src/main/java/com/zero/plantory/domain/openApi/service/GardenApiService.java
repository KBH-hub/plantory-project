package com.zero.plantory.domain.openApi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.zero.plantory.global.utils.NongsaroApiClient;
import com.zero.plantory.global.utils.XmlToJsonConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GardenApiService {

    private final NongsaroApiClient apiClient;
    private final XmlToJsonConverter converter;

    private static final String BASE_URL = "/service/garden";

    /** 광도 리스트 */
    public JsonNode getLightList() {
        String xml = apiClient.get(BASE_URL, "/lightList", null);
        return converter.convert(xml);
    }

    /** 생장 형태 */
    public JsonNode getGrwhstleList() {
        String xml = apiClient.get(BASE_URL, "/grwhstleList", null);
        return converter.convert(xml);
    }

    /** 잎 색 */
    public JsonNode getLefcolrList() {
        String xml = apiClient.get(BASE_URL, "/lefcolrList", null);
        return converter.convert(xml);
    }

    /** 잎 무늬 */
    public JsonNode getLefmrkList() {
        String xml = apiClient.get(BASE_URL, "/lefmrkList", null);
        return converter.convert(xml);
    }

    /** 꽃 색 */
    public JsonNode getFlclrList() {
        String xml = apiClient.get(BASE_URL, "/flclrList", null);
        return converter.convert(xml);
    }

    /** 열매 색 */
    public JsonNode getFmldecolrList() {
        String xml = apiClient.get(BASE_URL, "/fmldecolrList", null);
        return converter.convert(xml);
    }

    /** 발아 계절 */
    public JsonNode getIgnSeasonList() {
        String xml = apiClient.get(BASE_URL, "/ignSeasonList", null);
        return converter.convert(xml);
    }

    /** 월동 적정온도 */
    public JsonNode getWinterLwetList() {
        String xml = apiClient.get(BASE_URL, "/winterLwetList", null);
        return converter.convert(xml);
    }

    /** 가격 유형 */
    public JsonNode getPriceTypeList() {
        String xml = apiClient.get(BASE_URL, "/priceTypeList", null);
        return converter.convert(xml);
    }

    /** 물 주기 */
    public JsonNode getWaterCycleList() {
        String xml = apiClient.get(BASE_URL, "/waterCycleList", null);
        return converter.convert(xml);
    }

    /** 정원식물 목록 */
    public JsonNode getGardenList(String pageNo, String numOfRows, String lightCode) {

        Map<String, String> params = new HashMap<>();
        params.put("pageNo", pageNo);
        params.put("numOfRows", numOfRows);

        if (lightCode != null && !lightCode.isEmpty()) {
            params.put("lightCode", lightCode);
        }

        String xml = apiClient.get(BASE_URL, "/gardenList", params);
        return converter.convert(xml);
    }

    /** 정원식물 상세 */
    public JsonNode getGardenDtl(String cntntsNo) {
        Map<String, String> params = Map.of("cntntsNo", cntntsNo);
        String xml = apiClient.get(BASE_URL, "/gardenDtl", params);
        return converter.convert(xml);
    }

    /** 관련 이미지/파일 리스트 */
    public JsonNode getGardenFileList(String cntntsNo) {
        Map<String, String> params = Map.of("cntntsNo", cntntsNo);
        String xml = apiClient.get(BASE_URL, "/gardenFileList", params);
        return converter.convert(xml);
    }
}
