package com.zero.plantory.domain.member.controller;

import com.zero.plantory.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/members")
public class MemberRestController {

    private final MemberService memberService;

    @GetMapping("/checkMembername")
    public ResponseEntity<Map<String, Boolean>> checkMembername(
            @RequestParam String membername) {

        boolean isDuplicate = memberService.isDuplicateMembername(membername);
        return ResponseEntity.ok(Map.of("exists", isDuplicate));
    }

    @GetMapping("/checkNickname")
    public ResponseEntity<Map<String, Boolean>> checkNickname(
            @RequestParam String nickname) {

        boolean isDuplicate = memberService.isDuplicateNickname(nickname);
        return ResponseEntity.ok(Map.of("exists", isDuplicate));
    }
}

