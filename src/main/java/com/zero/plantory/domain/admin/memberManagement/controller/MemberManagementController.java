package com.zero.plantory.domain.admin.memberManagement.controller;

import com.zero.plantory.global.security.MemberDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/profile/")
public class MemberManagementController {

    @GetMapping("/{memberId}")
    public String publicProfile(
            @PathVariable Long memberId,
            Model model) {

        model.addAttribute("profileInfo", Map.of(
                "profileId", memberId
        ));

        return "/admin/adminProfileInfo";
    }
}
