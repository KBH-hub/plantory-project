package com.zero.plantory.domain.profile.controller;

import com.zero.plantory.global.security.MemberDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/profile")
@Slf4j
public class ProfileController {

    @GetMapping
    public String myProfile(@AuthenticationPrincipal MemberDetail memberDetail) {
        Long loginId = memberDetail.getMemberResponse().getMemberId();
        return "redirect:/profile/" + loginId;
    }

    @GetMapping("/{memberId}")
    public String publicProfile(
            @PathVariable Long memberId,
            @AuthenticationPrincipal MemberDetail memberDetail,
            Model model) {

        Long loginMemberId = memberDetail.getMemberResponse().getMemberId();
        boolean isMe = loginMemberId.equals(memberId);

        model.addAttribute("profileInfo", Map.of(
                "isMe", isMe,
                "profileId", memberId
        ));

        return "profile/profileInfo";
    }

    @GetMapping("update/{memberId}")
    public String updateProfile(
            @PathVariable Long memberId,
            @AuthenticationPrincipal MemberDetail memberDetail,
            Model model) {

        Long loginMemberId = memberDetail.getMemberResponse().getMemberId();
        boolean isMe = loginMemberId.equals(memberId);

//        log.info("updateProfile loginMemberId={} isMe={}", loginMemberId, isMe);

        model.addAttribute("profileInfo", Map.of(
                "isMe", isMe,
                "profileId", memberId
        ));

        return "profile/updateProfileInfo";
    }
}



