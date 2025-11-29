package com.zero.plantory.domain.sharing.controller;

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
}
