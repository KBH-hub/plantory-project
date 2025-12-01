package com.zero.plantory.domain.sharing.controller;

import com.zero.plantory.domain.sharing.dto.ReviewRequest;
import com.zero.plantory.domain.sharing.service.SharingScoreService;
import com.zero.plantory.global.security.MemberDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sharing")
@RequiredArgsConstructor
public class SharingScoreRestController {
    private final SharingScoreService sharingScoreService;

    @PostMapping("/{sharingId}/complete")
    public ResponseEntity<?> completeSharing(
            @PathVariable Long sharingId,
            @RequestParam Long targetMemberId,
            @AuthenticationPrincipal MemberDetail memberDetail
    ) {
        Long memberId = memberDetail.getMemberResponse().getMemberId();
        sharingScoreService.completeSharing(sharingId, memberId, targetMemberId);

        return ResponseEntity.ok(true);
    }

    @PostMapping("/{sharingId}/review")
    public ResponseEntity<?> registerReview(
            @PathVariable Long sharingId,
            @RequestBody ReviewRequest reviewRequest,
            @AuthenticationPrincipal MemberDetail memberDetail
    ) {

        Long loginUserId = memberDetail.getMemberResponse().getMemberId();

        sharingScoreService.registerSharingReview(
                sharingId,
                loginUserId,
                reviewRequest.getManner(),
                reviewRequest.getReShare(),
                reviewRequest.getSatisfaction()
        );

        return ResponseEntity.ok(true);
    }
}
