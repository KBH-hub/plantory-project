package com.zero.plantory.domain.openApi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
    private final ObjectMapper objectMapper;

    private static final String BASE_URL = "/service/garden";

    public JsonNode getLightList() {
        String xml = apiClient.get(BASE_URL, "/lightList", null);
        return converter.convert(xml);
    }

    public JsonNode getGrwhstleList() {
        String xml = apiClient.get(BASE_URL, "/grwhstleList", null);
        return converter.convert(xml);
    }

    public JsonNode getLefcolrList() {
        String xml = apiClient.get(BASE_URL, "/lefcolrList", null);
        return converter.convert(xml);
    }

    public JsonNode getLefmrkList() {
        String xml = apiClient.get(BASE_URL, "/lefmrkList", null);
        return converter.convert(xml);
    }

    public JsonNode getFlclrList() {
        String xml = apiClient.get(BASE_URL, "/flclrList", null);
        return converter.convert(xml);
    }

    public JsonNode getFmldecolrList() {
        String xml = apiClient.get(BASE_URL, "/fmldecolrList", null);
        return converter.convert(xml);
    }

    public JsonNode getIgnSeasonList() {
        String xml = apiClient.get(BASE_URL, "/ignSeasonList", null);
        return converter.convert(xml);
    }

    public JsonNode getWinterLwetList() {
        String xml = apiClient.get(BASE_URL, "/winterLwetList", null);
        return converter.convert(xml);
    }

    public JsonNode getPriceTypeList() {
        String xml = apiClient.get(BASE_URL, "/priceTypeList", null);
        return converter.convert(xml);
    }

    public JsonNode getWaterCycleList() {
        String xml = apiClient.get(BASE_URL, "/waterCycleList", null);
        return converter.convert(xml);
    }

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

    public JsonNode getGardenDtl(String cntntsNo) {
        Map<String, String> params = Map.of("cntntsNo", cntntsNo);
        String xml = apiClient.get(BASE_URL, "/gardenDtl", params);
        return converter.convert(xml);
    }

    public JsonNode getGardenFileList(String cntntsNo) {
        Map<String, String> params = Map.of("cntntsNo", cntntsNo);
        String xml = apiClient.get(BASE_URL, "/gardenFileList", params);
        return converter.convert(xml);
    }

    public ObjectNode getGardenDetail(String cntntsNo) {
        JsonNode files  = getGardenFileList(cntntsNo);
        JsonNode detail = getGardenDtl(cntntsNo);

        ObjectNode root = objectMapper.createObjectNode();
        root.set("files",  files);
        root.set("detail", detail);
        return root;
    }
}
