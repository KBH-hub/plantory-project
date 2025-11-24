package com.zero.plantory.domain.member.controller;

import com.zero.plantory.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/check-membername")
    public boolean checkMemberName(@RequestParam("membername") String membername){
        return memberService.isDuplicateMembername(membername);
    }

    @GetMapping("/check-nickname")
    public boolean checkNickName(@RequestParam("nickname") String nickname){
        return memberService.isDuplicateNickname(nickname);
    }

}
