package com.zero.plantory.domain.profile.controller;

import com.zero.plantory.domain.profile.dto.ProfileInfoResponse;
import com.zero.plantory.domain.profile.dto.PublicProfileResponse;
import com.zero.plantory.domain.profile.service.ProfileService;
import com.zero.plantory.global.security.MemberDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile")
public class ProfileRestController {
    private final ProfileService profileService;

    @GetMapping("/me")
    public ResponseEntity<ProfileInfoResponse> getProfile(@AuthenticationPrincipal MemberDetail memberDetail) {
        return ResponseEntity.ok(profileService.getProfileInfo(memberDetail.getMemberResponse().getMemberId()));
    }

    @GetMapping("/publicProfile/{memberId}")
    public ResponseEntity<PublicProfileResponse> getPublicProfile(@PathVariable Long memberId) {
        return ResponseEntity.ok(profileService.getPublicProfile(memberId));
    }
}
