package com.zero.plantory.domain.admin.weightManagement.controller;

import com.zero.plantory.domain.admin.weightManagement.dto.WeightLoggingResponse;
import com.zero.plantory.domain.admin.weightManagement.dto.WeightManagementResponse;
import com.zero.plantory.domain.admin.weightManagement.dto.WeightSaveRequest;
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
public class WeightManagementController {

    private final WeightManagementService service;

    @GetMapping("/list")
    public List<WeightManagementResponse> getMemberWeightList(
            @RequestParam(required = false) String keyword,
            @RequestParam int limit,
            @RequestParam int offset,
            @RequestParam(required = false) String range
    ) {
        return service.getMemberWeightList(keyword,limit,offset,range);
    }

    @PostMapping("/save")
    public void saveWeights(@AuthenticationPrincipal MemberDetail principal,
                            @RequestBody WeightSaveRequest req) {

        Long memberId = principal.getMemberResponse().getMemberId();

        service.saveWeights(memberId, req);
    }

    @GetMapping("/latest")
    public WeightLoggingResponse getLatestWeights() {
        return service.getLatestWeights();
    }

    @GetMapping("/careCounts")
    public ResponseEntity<?> getCareCounts() {
        return ResponseEntity.ok(service.getPlantsNeedingAttention());
    }

}
