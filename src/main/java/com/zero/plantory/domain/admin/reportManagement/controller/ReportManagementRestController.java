package com.zero.plantory.domain.admin.reportManagement.controller;

import com.zero.plantory.domain.admin.reportManagement.dto.IdListRequest;
import com.zero.plantory.domain.admin.reportManagement.dto.ReportManagementPageResponse;
import com.zero.plantory.domain.admin.reportManagement.dto.ReportManagementSearchRequest;
import com.zero.plantory.domain.admin.reportManagement.dto.ReportManagementResponse;
import com.zero.plantory.domain.admin.reportManagement.service.ReportManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reportManagement")
public class ReportManagementRestController {
//    softDelete
private final ReportManagementService reportManagementService;

        @GetMapping("/list")
        public ResponseEntity<ReportManagementPageResponse>  getReportList(ReportManagementSearchRequest request) {
            return ResponseEntity.ok().body(reportManagementService.getReporManagmentList(request));
        }

    @PutMapping("/softDelete")
    public ResponseEntity<?> softDelete(@RequestBody IdListRequest request) {

        int updated = reportManagementService.deleteReporManagmentList(request.getIds());

        if (updated > 0) {
            return ResponseEntity.ok("삭제 성공: " + updated + "건");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("삭제 실패: 삭제 대상 없음");
        }
    }

}
