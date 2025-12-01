package com.zero.plantory.domain.profile.controller;

import com.zero.plantory.domain.profile.dto.ProfileSharingHistoryListRequest;
import com.zero.plantory.domain.profile.dto.ProfileSharingHistoryListResponse;
import com.zero.plantory.domain.profile.dto.ProfileSharingHistoryPageResponse;
import com.zero.plantory.domain.profile.service.ProfileSharingHistoryService;
import com.zero.plantory.global.security.MemberDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("/my")
    public ProfileSharingHistoryPageResponse getMySharing(
            @AuthenticationPrincipal MemberDetail user,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "전체") String status,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "10") Integer limit
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
            @AuthenticationPrincipal MemberDetail user,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "전체") String status,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "10") Integer limit
    ) {
        ProfileSharingHistoryListRequest request = ProfileSharingHistoryListRequest.builder()
                .memberId(user.getMemberResponse().getMemberId())
                .myType("RECEIVED")
                .keyword(keyword)
                .status(status)
                .offset(offset)
                .limit(limit)
                .build();

        return profileSharingHistoryService.getProfileSharingHistoryList(request);
    }


}
