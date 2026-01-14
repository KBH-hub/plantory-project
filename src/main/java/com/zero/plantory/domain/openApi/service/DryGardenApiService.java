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

    public JsonNode getClList() {
        String xml = client.get(BASE_URL, "/clList", null);
        return converter.convert(xml);
    }

    public JsonNode getStleSeList() {
        String xml = client.get(BASE_URL, "/stleSeList", null);
        return converter.convert(xml);
    }

    public JsonNode getRdxStleList() {
        String xml = client.get(BASE_URL, "/rdxStleList", null);
        return converter.convert(xml);
    }

    public JsonNode getGrwtseVeList() {
        String xml = client.get(BASE_URL, "/grwtseVeList", null);
        return converter.convert(xml);
    }

    public JsonNode getManageLevelList() {
        String xml = client.get(BASE_URL, "/manageLevelList", null);
        return converter.convert(xml);
    }

    public JsonNode getManageDemandList() {
        String xml = client.get(BASE_URL, "/manageDemandList", null);
        return converter.convert(xml);
    }

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

    public JsonNode getDryGardenDetail(String cntntsNo) {

        String xml = client.get(
                BASE_URL,
                "/dryGardenDtl",
                Map.of("cntntsNo", cntntsNo)
        );

        return converter.convert(xml);
    }
}
