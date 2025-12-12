package com.zero.plantory.domain.admin.weightManagement.controller;

import com.zero.plantory.domain.admin.weightManagement.dto.*;
import com.zero.plantory.domain.admin.weightManagement.service.WeightManagementService;
import com.zero.plantory.global.security.MemberDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/weightManagement")
@RequiredArgsConstructor
public class WeightManagementRestController {

    private final WeightManagementService weightManagementService;

    @GetMapping("/list")
    public List<WeightManagementResponse> getMemberWeightList(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(required = false) String range
    ) {
        return weightManagementService.getMemberWeightList(keyword,limit,offset,range);
    }

    @PostMapping("/list")
    public void saveWeights(@AuthenticationPrincipal MemberDetail principal,
                            @RequestBody SaveWeightRequest saveWeightRequest) {

        Long memberId = principal.getMemberResponse().getMemberId();

        weightManagementService.saveWeights(memberId, saveWeightRequest);
    }

    @GetMapping("/latest")
    public WeightLoggingResponse getLatestWeights() {
        return weightManagementService.getLatestWeights();
    }

    @GetMapping("/careCounts")
    public ResponseEntity<?> getCareCounts() {
        return ResponseEntity.ok(weightManagementService.getPlantsNeedingAttention());
    }

    @GetMapping("/rate")
    public ResponseEntity<RateResponse> getRate() { return ResponseEntity.ok(weightManagementService.getRate());}

    @PostMapping("/rate")
    public void saveRate(@AuthenticationPrincipal MemberDetail principal,
                            @RequestBody SaveRateRequest saveRateRequest) {

        Long memberId = principal.getMemberResponse().getMemberId();

        weightManagementService.saveRate(memberId, saveRateRequest);
    }
}
