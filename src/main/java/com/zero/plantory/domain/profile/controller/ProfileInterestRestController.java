package com.zero.plantory.domain.profile.controller;

import com.zero.plantory.domain.profile.dto.ProfileInterestListRequest;
import com.zero.plantory.domain.profile.dto.ProfileSharingResponse;
import com.zero.plantory.domain.profile.service.ProfileInterestService;
import com.zero.plantory.global.security.MemberDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profileInterest")
public class ProfileInterestRestController {

    private final ProfileInterestService profileInterestService;

    @GetMapping
    public ResponseEntity<List<ProfileSharingResponse>> getProfileInterest(
            @AuthenticationPrincipal MemberDetail memberDetail,
            @RequestParam(required = false) String keyword,
            @RequestParam int limit,
            @RequestParam int offset
    ) {
        ProfileInterestListRequest request = new ProfileInterestListRequest();
        request.setMemberId(memberDetail.getMemberResponse().getMemberId());
        request.setKeyword(keyword);
        request.setLimit(limit);
        request.setOffset(offset);

        List<ProfileSharingResponse> list =
                profileInterestService.getProfileInterest(request);

        return ResponseEntity.ok(list);
    }

}
