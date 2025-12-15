package com.zero.plantory.domain.dashboard.controller;

import com.zero.plantory.domain.dashboard.dto.DashboardSummaryResponse;
import com.zero.plantory.domain.dashboard.service.DashboardService;
import com.zero.plantory.global.security.MemberDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/dashboard")
@RequiredArgsConstructor
public class DashboardRestController {
    private final DashboardService dashboardService;

    @GetMapping
    public DashboardSummaryResponse getDashboard(@AuthenticationPrincipal MemberDetail memberDetail) {
        return dashboardService.getDashboardSummary(memberDetail.getMemberResponse().getMemberId());
    }

}
