package com.zero.plantory.domain.admin.memberManagement.controller;

import com.zero.plantory.domain.admin.memberManagement.dto.MemberManagementResponse;
import com.zero.plantory.domain.admin.memberManagement.service.MemberManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/memberManagement")
public class MemberManagementRestController {

    private final MemberManagementService  memberManagementService;

    @GetMapping("/members")
    public List<MemberManagementResponse> getMemberList(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset
    ) {
        log.info("getMemberList 로고 결과: "+ keyword);
        log.info("getMemberList 로고 결과: "+ limit);
        log.info("getMemberList 로고 결과: "+ offset);
        return memberManagementService.getMemberList(keyword, limit, offset);
    }
}
