package com.zero.plantory.domain.sharing.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.zero.plantory.domain.openApi.service.DryGardenApiService;
import com.zero.plantory.domain.openApi.service.GardenApiService;
import com.zero.plantory.domain.sharing.dto.DictionaryModalSearchRequest;
import com.zero.plantory.domain.sharing.dto.DictionaryModalSearchlResponse;
import com.zero.plantory.domain.sharing.service.DictionaryModalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dictionaryModal")
@RequiredArgsConstructor
public class DictionaryModalRestController {
    private final GardenApiService gardenApiService;
    private final DryGardenApiService dryGardenApiService;
    private final DictionaryModalService dictionaryModalService;

    @GetMapping("/search")
    public List<DictionaryModalSearchRequest> searchPlants(@RequestParam String word) {
        return dictionaryModalService.search(word);
    }

    @GetMapping("/garden/{cntntsNo}")
    public DictionaryModalSearchlResponse getGardenDetail(@PathVariable String cntntsNo) {

        JsonNode node = gardenApiService.getGardenDetail(cntntsNo);

        return dictionaryModalService.convertGardenDetail(node);
    }

    @GetMapping("/dry/{cntntsNo}")
    public DictionaryModalSearchlResponse getDryDetail(@PathVariable String cntntsNo) {

        JsonNode node = dryGardenApiService.getDryGardenDetail(cntntsNo);

        return dictionaryModalService.convertDryDetail(node);
    }


}
