package com.zero.plantory.domain.admin.reportManagement.controller;

import com.zero.plantory.domain.admin.reportManagement.dto.ReportManagementSearchRequest;
import com.zero.plantory.domain.admin.reportManagement.dto.ReportResponse;
import com.zero.plantory.domain.admin.reportManagement.service.ReportManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reportManagement")
public class ReportManagementRestController {

private final ReportManagementService reportManagementService;

        @GetMapping("/list")
        public ResponseEntity<List<ReportResponse>>  getReportList(
                @RequestParam(value = "keyword", required = false) String keyword,
                @RequestParam(value = "status", required = false) String status,
                @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                @RequestParam(value = "offset", defaultValue = "0") Integer offset
        ) {
            ReportManagementSearchRequest req = new ReportManagementSearchRequest(
                    keyword, status, offset, limit
            );
            return ResponseEntity.ok().body(reportManagementService.getReporManagmentList(req));
        }

}
