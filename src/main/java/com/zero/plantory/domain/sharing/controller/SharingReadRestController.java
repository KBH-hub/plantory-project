package com.zero.plantory.domain.sharing.controller;

import com.zero.plantory.domain.sharing.dto.*;
import com.zero.plantory.domain.sharing.dto.SharingHistoryResponse;
import com.zero.plantory.domain.sharing.dto.SharingSearchRequest;
import com.zero.plantory.domain.sharing.service.SharingReadService;
import com.zero.plantory.global.security.MemberDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sharing")
@RequiredArgsConstructor
public class SharingReadRestController {

    private final SharingReadService sharingReadService;


    @GetMapping
    public List<SharingCardListResponse> getSharingList(SharingSearchRequest request) {
        return sharingReadService.getSharingList(request);
    }

    @GetMapping("/countInterest")
    public int countInterest(@RequestParam Long memberId) {
        return sharingReadService.countInterest(memberId);
    }

    @GetMapping("/popular")
    public List<SharingPopularResponse> getPopularSharingList(SharingSearchRequest request) {
        return sharingReadService.getPopularSharingList(request);
    }


    @GetMapping("/{sharingId}")
    public SelectSharingDetailResponse getSharingDetail(@PathVariable Long sharingId) {
        return sharingReadService.getSharingDetail(sharingId);
    }

    @GetMapping("/{sharingId}/comments")
    public List<SelectCommentListResponse> getSharingComments(@PathVariable Long sharingId) {
        return sharingReadService.getSharingComments(sharingId);
    }

    @GetMapping("/{sharingId}/partners")
    public List<SharingPartnerResponse> getMessagePartners(
            @PathVariable Long sharingId,
            @RequestParam Long receiverId
    ) {
        return sharingReadService.getMessagePartners(receiverId, sharingId);
    }

    @GetMapping("/history/given")
    public List<SharingHistoryResponse> getMySharingGiven(@RequestParam Long memberId) {
        return sharingReadService.getMySharingGiven(memberId);
    }

    @GetMapping("/history/received")
    public List<SharingHistoryResponse> getMySharingReceived(@RequestParam Long memberId) {
        return sharingReadService.getMySharingReceived(memberId);
    }

    @GetMapping("/{sharingId}/reviewInfo")
    public ResponseEntity<ReviewInfoResponse> getReviewInfo(
            @PathVariable Long sharingId,
            @AuthenticationPrincipal MemberDetail memberDetail
    ) {
        Long memberId = memberDetail.getMemberResponse().getMemberId();
        ReviewInfoResponse result = sharingReadService.getReviewInfo(sharingId, memberId);

        return ResponseEntity.ok(result);
    }


}

