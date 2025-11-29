package com.zero.plantory.domain.openApi.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zero.plantory.domain.openApi.service.DryGardenApiService;
import com.zero.plantory.domain.openApi.service.GardenApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dictionary/garden")
@RequiredArgsConstructor
public class DictionaryRestController {

    private final GardenApiService gardenApiService;

    @GetMapping
    public JsonNode getDictionary( @RequestParam(defaultValue = "1") String pageNo,
                                   @RequestParam(defaultValue = "10") String numOfRows,
                                   @RequestParam(required = false) String lightCode) {
        return gardenApiService.getGardenList(pageNo, numOfRows, lightCode);
    }

    @GetMapping("/{cntntsNo}")
    public JsonNode getGarden(@PathVariable String cntntsNo) {
        return gardenApiService.getGardenDetail(cntntsNo);
    }

}
