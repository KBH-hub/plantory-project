package com.zero.plantory.domain.profile.controller;

import com.zero.plantory.domain.image.service.ImageService;
import com.zero.plantory.domain.profile.dto.ProfileSharingHistoryListRequest;
import com.zero.plantory.domain.profile.dto.ProfileSharingHistoryListResponse;
import com.zero.plantory.domain.profile.dto.ProfileSharingHistoryPageResponse;
import com.zero.plantory.domain.profile.service.ProfileSharingHistoryService;
import com.zero.plantory.global.dto.ImageDTO;
import com.zero.plantory.global.dto.ImageTargetType;
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
@RequiredArgsConstructor
@RestController
@RequestMapping("api/profileSharing/")
public class ProfileSharingHistoryRestController {

    private final ProfileSharingHistoryService profileSharingHistoryService;
    private final ImageService imageService;

    @GetMapping("/my")
    public ProfileSharingHistoryPageResponse getMySharing(
            @AuthenticationPrincipal MemberDetail user,
            @RequestParam(required = false) String keyword,
            @RequestParam String status,
            @RequestParam Integer offset,
            @RequestParam Integer limit
    ) {
        ProfileSharingHistoryListRequest request = ProfileSharingHistoryListRequest.builder()
                .memberId(user.getMemberResponse().getMemberId())
                .keyword(keyword)
                .status(status)
                .offset(offset)
                .limit(limit)
                .myType("MY")
                .build();

        return profileSharingHistoryService.getProfileSharingHistoryList(request);
    }

    @GetMapping("/received")
    public ProfileSharingHistoryPageResponse getReceivedSharing(
            @AuthenticationPrincipal MemberDetail memberDetail,
            @RequestParam(required = false) String keyword,
            @RequestParam String status,
            @RequestParam int offset,
            @RequestParam int limit
    ) {
        ProfileSharingHistoryListRequest request = ProfileSharingHistoryListRequest.builder()
                .memberId(memberDetail.getMemberResponse().getMemberId())
                .keyword(keyword)
                .status(status)
                .offset(offset)
                .limit(limit)
                .myType("RECEIVED")
                .build();

        return profileSharingHistoryService.getProfileSharingHistoryList(request);
    }
}
