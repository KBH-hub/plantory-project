package com.zero.plantory.domain.admin.memberManagement.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class MemberManagementController {

    @GetMapping("/profile/{memberId}")
    public String adminProfile(
            @PathVariable Long memberId,
            Model model) {

        model.addAttribute("profileInfo", Map.of(
                "profileId", memberId
        ));

        return "/admin/adminProfileInfo";
    }

    @GetMapping("/readSharing/{sharingId}")
    public String readSharing(@PathVariable Long sharingId, Model model){
        model.addAttribute("sharingId", sharingId);
        return "/admin/adminReadSharing";
    }
}
