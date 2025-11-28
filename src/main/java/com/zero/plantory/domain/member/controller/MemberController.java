package com.zero.plantory.domain.member.controller;

import ch.qos.logback.core.model.Model;
import com.zero.plantory.domain.member.dto.MemberSignUpRequest;
import com.zero.plantory.domain.member.service.MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
