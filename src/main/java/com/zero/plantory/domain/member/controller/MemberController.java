package com.zero.plantory.domain.member.controller;

import com.zero.plantory.domain.profile.dto.MemberSignUpRequest;
import com.zero.plantory.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signUp")
    public String signUp(MemberSignUpRequest request) {
        try {
            memberService.signUp(request);
            return "redirect:/login";
        } catch (IllegalStateException e) {
            return "redirect:/signUp?error=" + e.getMessage();
        }

    }

}
