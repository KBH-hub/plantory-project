package com.zero.plantory.domain.myPlant.controller;

import com.zero.plantory.domain.myPlant.dto.MyPlantRequest;
import com.zero.plantory.domain.myPlant.dto.MyPlantResponse;
import com.zero.plantory.domain.myPlant.service.MyPlantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/myPlant")
@RequiredArgsConstructor
public class MyplantController {

    private final MyPlantService myPlantService;

    @GetMapping("/list")
    public List<MyPlantResponse> getMyPlantList(@RequestParam Long memberId, @RequestParam String name, @RequestParam Integer limit, @RequestParam Integer offset) {
        return myPlantService.getMyPlantList(memberId, name, limit, offset);
    }

    @GetMapping("/list/name")
    public List<MyPlantResponse> getMyPlantListByName(@RequestParam Long memberId, @RequestParam String name) {
        return myPlantService.getMyPlantByName(memberId, name);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> addMyPlant(@RequestBody MyPlantRequest myPlantRequest) {
        if(myPlantService.registerMyPlant(myPlantRequest) == 0)
            return ResponseEntity.status(400).body(Map.of("message", "myPlant regist fail"));
        return ResponseEntity.ok().body(Map.of("message", "myPlant regist success"));
    }

    @PutMapping
    public ResponseEntity<Map<String, String>> updateMyPlant(@RequestBody MyPlantRequest myPlantRequest) {
        if(myPlantService.updateMyPlant(myPlantRequest) == 0)
            return ResponseEntity.status(400).body(Map.of("message", "myPlant regist fail"));
        return ResponseEntity.ok().body(Map.of("message", "myPlant regist success"));
    }

    @DeleteMapping
    public ResponseEntity<Map<String, String>> removeMyPlant(@RequestBody Long myplantId) {
        if(myPlantService.removePlant(myplantId) == 0)
            return ResponseEntity.status(400).body(Map.of("message", "myPlant regist fail"));
        return ResponseEntity.ok().body(Map.of("message", "myPlant regist success"));
    }

}
