package com.zero.plantory.domain.report.controller;

import com.zero.plantory.domain.report.dto.NameListResponse;
import com.zero.plantory.domain.report.dto.ReportRequest;
import com.zero.plantory.domain.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportRestController {
    private final ReportService reportService;

    @GetMapping("/users")
    public ResponseEntity<List<NameListResponse>> searchUserByNickname(@RequestParam String nickname, @RequestParam Long viewerId) {
        return ResponseEntity.ok().body(reportService.getUsersIdByNickname(nickname, viewerId));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String,String>> registerReport(
            @ModelAttribute ReportRequest request,                 // 폼 필드 → 객체 바인딩
            @RequestParam("files") List<MultipartFile> files       // key 이름: files
    ) throws IOException {
        int saved = reportService.registerReport(request, files);
        int expected = (files == null ? 0 : files.size()) + 1;     // report 1건 + 이미지 N건
        if (saved == expected) {
            return ResponseEntity.ok(Map.of("message", "registry report success"));
        }
        return ResponseEntity.badRequest().body(Map.of("message", "register report fail"));
    }
}
