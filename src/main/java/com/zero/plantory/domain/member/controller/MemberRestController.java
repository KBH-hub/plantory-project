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

    @GetMapping("/exists")
    public ResponseEntity<Map<String, Object>> checkExists(
            @RequestParam(required = false) String membername,
            @RequestParam(required = false) String nickname) {

        if (membername != null && memberService.isDuplicateMembername(membername)) {
            return ResponseEntity.ok(Map.of(
                    "code", "DUPLICATE_MEMBERNAME",
                    "target", "membername",
                    "message", "이미 사용 중인 아이디입니다."
            ));
        }

        if (nickname != null && memberService.isDuplicateNickname(nickname)) {
            return ResponseEntity.ok(Map.of(
                    "code", "DUPLICATE_NICKNAME",
                    "target", "nickname",
                    "message", "이미 사용 중인 닉네임입니다."
            ));
        }

        return ResponseEntity.ok(Map.of(
                "code", "OK",
                "message", "사용 가능합니다."
        ));
    }



}
