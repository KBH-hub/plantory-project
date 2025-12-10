package com.zero.plantory.domain.profile.controller;

import com.zero.plantory.domain.profile.dto.ProfileWrittenDeleteRequest;
import com.zero.plantory.domain.profile.dto.ProfileWrittenListRequest;
import com.zero.plantory.domain.profile.mapper.ProfileContentMapper;
import com.zero.plantory.domain.profile.service.ProfileContentService;
import com.zero.plantory.domain.profile.service.ProfileService;
import com.zero.plantory.domain.profile.service.ProfileWrittenPageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profileWritten")
public class ProfileWrittenRestController {

    private final ProfileContentService profileContentService;

    @GetMapping("/{memberId}")
    public ResponseEntity<ProfileWrittenPageResult> getWrittenList(
            @PathVariable Long memberId,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "ALL") String category,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset
    ) {

        ProfileWrittenListRequest profileContentServiceProfileWrittenList = ProfileWrittenListRequest.builder()
                .memberId(memberId)
                .keyword(keyword)
                .limit(limit)
                .offset(offset)
                .build();

//        log.info("ProfileWrittenListRequest로그:" + profileContentServiceProfileWrittenList.toString());
//        log.info(category);

        return ResponseEntity.ok(
                profileContentService.getProfileWrittenList(profileContentServiceProfileWrittenList, category)
        );
    }

    @PostMapping("/softDelete")
    public ResponseEntity<?> deleteWritten(@RequestBody ProfileWrittenDeleteRequest request) {
        profileContentService.deleteWritten(request);
        return ResponseEntity.ok().build();
    }

}
