package com.zero.plantory.domain.admin.memberManagement.controller;

import com.zero.plantory.domain.admin.memberManagement.dto.MemberManagementPageResponse;
import com.zero.plantory.domain.admin.memberManagement.service.MemberManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/memberManagement")
public class MemberManagementRestController {

    private final MemberManagementService  memberManagementService;

    @GetMapping("/members")
    public MemberManagementPageResponse getMemberList(
            @RequestParam(required = false) String keyword,
            @RequestParam int limit,
            @RequestParam int offset
    ) {
        return memberManagementService.getMemberList(keyword, limit, offset);
    }
}
