package com.zero.plantory.domain.myProfile.controller;

import com.zero.plantory.domain.myProfile.dto.MyInfoResponse;
import com.zero.plantory.domain.myProfile.service.MyProfileService;
import com.zero.plantory.global.security.MemberDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/myProfile")
public class MyProfileRestController {
    private final MyProfileService myprofileService;

    @GetMapping
    public ResponseEntity<MyInfoResponse> getMyProfile(@AuthenticationPrincipal MemberDetail memberDetail) {
        return ResponseEntity.ok(myprofileService.getMyInfo(memberDetail.getMemberResponse().getMemberId()));
    }
}
