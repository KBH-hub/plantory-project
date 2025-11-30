package com.zero.plantory.domain.profile.controller;

import com.zero.plantory.domain.profile.dto.PasswordChangeRequest;
import com.zero.plantory.domain.profile.dto.ProfileInfoResponse;
import com.zero.plantory.domain.profile.dto.ProfileUpdateRequest;
import com.zero.plantory.domain.profile.dto.PublicProfileResponse;
import com.zero.plantory.domain.profile.service.ProfileService;
import com.zero.plantory.global.security.MemberDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
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

    @PutMapping("/changePassword")
    public ResponseEntity<?> changePassword(
            @AuthenticationPrincipal MemberDetail memberDetail,
            @RequestBody PasswordChangeRequest req) {

        log.info("비밀번호 변경 객체로그"+req.toString());

        boolean success = profileService.changePassword(
                memberDetail.getMemberResponse().getMemberId(),
                req.getOldPassword(),
                req.getNewPassword()
        );

        return ResponseEntity.ok(Map.of("success", success));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> softWithdraw(
            @AuthenticationPrincipal MemberDetail memberDetail) {

        Long memberId = memberDetail.getMemberResponse().getMemberId();
        profileService.deleteMemberById(memberId);

        return ResponseEntity.ok("회원 탈퇴 완료");
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(
            @AuthenticationPrincipal MemberDetail memberDetail,
            @RequestBody ProfileUpdateRequest profileUpdateRequest) {

        Long memberId = memberDetail.getMemberResponse().getMemberId();
        profileUpdateRequest.setMemberId(memberId);

        profileService.updateProfileInfo(profileUpdateRequest);

        return ResponseEntity.ok("success");
    }





}
