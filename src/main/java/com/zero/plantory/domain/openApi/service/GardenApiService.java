package com.zero.plantory.domain.openApi.service;

import com.zero.plantory.global.utils.NongsaroApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GardenApiService {

    private final NongsaroApiClient apiClient;

    private static final String BASE_URL = "/service/garden";

    public String getLightList() {
        return apiClient.get(BASE_URL, "/lightList", null);
    }

    public String getGrwhstleList() {
        return apiClient.get(BASE_URL, "/grwhstleList", null);
    }

    public String getLefcolrList() {
        return apiClient.get(BASE_URL, "/lefcolrList", null);
    }

    public String getLefmrkList() {
        return apiClient.get(BASE_URL, "/lefmrkList", null);
    }

    public String getFlclrList() {
        return apiClient.get(BASE_URL, "/flclrList", null);
    }

    public String getFmldecolrList() {
        return apiClient.get(BASE_URL, "/fmldecolrList", null);
    }

    public String getIgnSeasonList() {
        return apiClient.get(BASE_URL, "/ignSeasonList", null);
    }

    public String getWinterLwetList() {
        return apiClient.get(BASE_URL, "/winterLwetList", null);
    }

    public String getPriceTypeList() {
        return apiClient.get(BASE_URL, "/priceTypeList", null);
    }

    public String getWaterCycleList() {
        return apiClient.get(BASE_URL, "/waterCycleList", null);
    }

    public String getGardenList(String pageNo, String numOfRows, String lightCode) {

        Map<String, String> params = new HashMap<>();
        params.put("pageNo", pageNo);
        params.put("numOfRows", numOfRows);
        if(lightCode != null) params.put("lightCode", lightCode);

        return apiClient.get(BASE_URL, "/gardenList", params);
    }

    public String getGardenDtl(String cntntsNo) {
        Map<String, String> params = Map.of("cntntsNo", cntntsNo);
        return apiClient.get(BASE_URL, "/gardenDtl", params);
    }

    public String getGardenFileList(String cntntsNo) {
        Map<String, String> params = Map.of("cntntsNo", cntntsNo);
        return apiClient.get(BASE_URL, "/gardenFileList", params);
    }
}
