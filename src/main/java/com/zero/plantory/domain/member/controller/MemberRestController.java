package com.zero.plantory.domain.member.controller;

import com.zero.plantory.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/exists")
    public ResponseEntity<Map<String, Object>> checkExists(
            @RequestParam(required = false) String membername,
            @RequestParam(required = false) String nickname) {
        boolean exists = false;
        if (membername != null) {
            exists = memberService.isDuplicateMembername(membername);
        } else if (nickname != null) {
            exists = memberService.isDuplicateNickname(nickname);
        }
        if (exists) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "exists", true,
                            "message", "이미 사용 중 입니다. "
                    ));
        }

        return ResponseEntity
                .ok(Map.of(
                        "exists", false,
                        "message", "사용 가능합니다."
                ));
    }


}
