package com.zero.plantory.domain.sharing.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.zero.plantory.domain.openApi.service.DryGardenApiService;
import com.zero.plantory.domain.openApi.service.GardenApiService;
import com.zero.plantory.domain.sharing.dto.DictionaryModalSearchRequest;
import com.zero.plantory.domain.sharing.dto.DictionaryModalSearchlResponse;
import com.zero.plantory.global.dto.ManageDemand;
import com.zero.plantory.global.dto.ManageLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DictionaryModalService {

    private final GardenApiService gardenApiService;
    private final DryGardenApiService dryGardenApiService;

    public List<DictionaryModalSearchRequest> search(String word) {

        List<DictionaryModalSearchRequest> result = new ArrayList<>();

        JsonNode gardenNode = gardenApiService.getGardenList("1", "100", null);
        result.addAll(convertGardenList(gardenNode, word));

        JsonNode dryNode = dryGardenApiService.getDryGardenList("1", "100", "");
        result.addAll(convertDryList(dryNode, word));

        return result;
    }

    private List<DictionaryModalSearchRequest> convertGardenList(JsonNode node, String word) {
        List<DictionaryModalSearchRequest> list = new ArrayList<>();

        JsonNode items = node.path("body").path("items").path("item");
        if (items.isMissingNode()) return list;

        for (JsonNode item : items) {
            String name = item.path("cntntsSj").asText("");
            if (!name.contains(word)) continue;

            String id = item.path("cntntsNo").asText();

            String thumb = item.path("rtnThumbFileUrl").asText("");
            if (thumb.contains("|")) thumb = thumb.split("\\|")[0];

            list.add(new DictionaryModalSearchRequest(id, name, thumb, "garden"));
        }

        return list;
    }

    private List<DictionaryModalSearchRequest> convertDryList(JsonNode node, String word) {
        List<DictionaryModalSearchRequest> list = new ArrayList<>();

        JsonNode items = node.path("body").path("items").path("item");
        if (items.isMissingNode()) return list;

        for (JsonNode item : items) {
            String name = item.path("cntntsSj").asText("");
            if (!name.contains(word)) continue;

            String id = item.path("cntntsNo").asText();
            String thumb = item.path("thumbImgUrl1").asText("");

            list.add(new DictionaryModalSearchRequest(id, name, thumb, "dry"));
        }

        return list;
    }

    private ManageLevel convertLevel(String raw) {
        if (raw == null) return ManageLevel.ETC;
        return switch (raw.trim()) {
            case "매우 쉬움" -> ManageLevel.VERY_EASY;
            case "쉬움", "초보자" -> ManageLevel.EASY;
            case "보통" -> ManageLevel.NORMAL;
            case "어려움", "경험자" -> ManageLevel.HARD;
            case "매우 어려움", "전문가" -> ManageLevel.VERY_HARD;
            default -> ManageLevel.ETC;
        };
    }

    private ManageDemand convertDemand(String raw) {
        if (raw == null) return ManageDemand.ETC;
        return switch (raw.trim()) {
            case "잘 견딤", "낮음 (잘 견딤)" -> ManageDemand.STRONG;
            case "약간 돌봄", "보통 (약간 잘 견딤)" -> ManageDemand.LITTLE_CARE;
            case "필요함" -> ManageDemand.NEED_CARE;
            case "특별관리요구" -> ManageDemand.SPECIAL_CARE;
            default -> ManageDemand.ETC;
        };
    }

    public DictionaryModalSearchlResponse convertGardenDetail(JsonNode node) {

        JsonNode files = node.path("files").path("body").path("items").path("item");

        String name = "";
        String fileUrl = null;

        if (files.isArray() && files.size() > 0) {
            JsonNode first = files.get(0);
            name = first.path("cntntsSj").asText("");
            fileUrl = first.path("rtnThumbFileUrl").asText(null);
        }

        JsonNode item = node.path("detail").path("body").path("item");

        String levelName = item.path("managelevelCodeNm").asText();
        String demandName = item.path("managedemanddoCodeNm").asText();

        ManageLevel level = convertLevel(levelName);
        ManageDemand demand = convertDemand(demandName);

        return DictionaryModalSearchlResponse.builder()
                .plantName(name)
                .fileUrl(fileUrl)
                .manageLevel(level)
                .levelLabel(level.getLabel())
                .manageDemand(demand)
                .demandLabel(demand.getLabel())
                .build();
    }



    public DictionaryModalSearchlResponse convertDryDetail(JsonNode node) {

        JsonNode item = node.path("body").path("item");

        String name = item.path("cntntsSj").asText("");

        String fileUrl = item.path("mainImgUrl1").asText("");
        if (fileUrl.isEmpty()) {
            fileUrl = item.path("lightImgUrl1").asText("");
        }

        String levelName = item.path("manageLevelNm").asText("");
        String demandName = item.path("manageDemandNm").asText("");

        ManageLevel level = convertLevel(levelName);
        ManageDemand demand = convertDemand(demandName);
        System.out.println(node.toPrettyString());

        return DictionaryModalSearchlResponse.builder()
                .plantName(name)
                .fileUrl(fileUrl)
                .manageLevel(level)
                .levelLabel(level.getLabel())
                .manageDemand(demand)
                .demandLabel(demand.getLabel())
                .build();
    }



}
