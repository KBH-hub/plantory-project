package com.zero.plantory.domain.plantingCalendar.controller;

import com.zero.plantory.domain.plantingCalendar.dto.DiaryResponse;
import com.zero.plantory.domain.plantingCalendar.dto.PlantingCalendarResponse;
import com.zero.plantory.domain.plantingCalendar.service.PlantingCalenderService;
import com.zero.plantory.global.dto.ImageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/plantingCalender")
@RequiredArgsConstructor
public class PlantingCalenderRestController {

    private final PlantingCalenderService plantingCalenderService;

    @GetMapping("/diary")
    public List<PlantingCalendarResponse> getPlantingDiaryCalendar(@RequestParam Long memberId,
                                                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return plantingCalenderService.getDiaryCalendar(memberId, startDate, endDate);
    }

    @GetMapping("/watering")
    public List<PlantingCalendarResponse> getPlantingWateringCalendar(@RequestParam Long memberId,
                                                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return plantingCalenderService.getWateringCalendar(memberId, startDate, endDate);
    }

    @PutMapping("/watering")
    public ResponseEntity<Map<String, String>> updateWateringFlag(@RequestParam Long wateringId) {
        int result = plantingCalenderService.updatePlantWateringCheck(wateringId);
        if (result == 0) {
            return ResponseEntity.status(400).body(Map.of("message", "check fail"));
        }
        return ResponseEntity.status(200).body(Map.of("message", "check success"));
    }

    @GetMapping("/diaryInfo/{diaryId}")
    public ResponseEntity<?> getDiaryUpdateModalInfo(@PathVariable Long diaryId) {

        DiaryResponse diary = plantingCalenderService.findDiaryUpdateInfo(diaryId);
        if (diary == null) {
            return ResponseEntity.status(400)
                    .body(Map.of(
                            "message", "diary not found",
                            "diaryId", diaryId
                    ));
        }

        List<ImageDTO> images = plantingCalenderService.findDiaryUpdateImageInfo(diaryId);

        return ResponseEntity.ok(
                Map.of(
                        "diary", diary,
                        "images", images
                )
        );
    }

    @DeleteMapping("/diary/{diaryId}")
    public ResponseEntity<Map<String, String>> deleteDiary(@PathVariable Long diaryId) {
        if(plantingCalenderService.removeDiary(diaryId) == 0)
            return ResponseEntity.status(400).body(Map.of("message", "diary delete fail"));
        return ResponseEntity.status(200).body(Map.of("message", "diary delete success"));
    }


}
