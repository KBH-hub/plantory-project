package com.zero.plantory.domain.profile.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @GetMapping("/me")
    public String profile(Model model) {
        model.addAttribute("isMe", true);
        return "profile/profileInfo";
    }

    @GetMapping("/{memberId}")
    public String publicProfile(@PathVariable Long memberId, Model model) {
        model.addAttribute("profileId", memberId);
        model.addAttribute("isMe", false);
        return "profile/profileInfo";
    }
}


