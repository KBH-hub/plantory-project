package com.zero.plantory.domain.sharing.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zero.plantory.domain.sharing.dto.CommentRequest;
import com.zero.plantory.domain.sharing.dto.SharingRequest;
import com.zero.plantory.domain.sharing.mapper.SharingMapper;
import com.zero.plantory.domain.sharing.service.SharingWriteService;
import com.zero.plantory.global.security.MemberDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/sharing")
@RequiredArgsConstructor
public class SharingWriteRestController {

    private final SharingWriteService sharingWriteService;
    private final SharingMapper sharingMapper;

    @PostMapping
    public ResponseEntity<?> createSharing(
            @ModelAttribute SharingRequest request,
            @RequestParam(value = "files", required = false) List<MultipartFile> files) throws IOException {

        Long sharingId = sharingWriteService.registerSharing(request, files);
        return ResponseEntity.ok(sharingId);
    }

    @PutMapping("/{sharingId}")
    public ResponseEntity<?> updateSharing(
            @PathVariable Long sharingId,
            @ModelAttribute SharingRequest request,
            @RequestParam(value = "files", required = false) List<MultipartFile> files) throws IOException {

        request.setSharingId(sharingId);

        if (request.getDeletedImageIds() != null && !request.getDeletedImageIds().isBlank()) {
            ObjectMapper mapper = new ObjectMapper();

            List<Long> ids = mapper.readValue(
                    request.getDeletedImageIds(),
                    new TypeReference<List<Long>>() {}
            );

            request.setDeletedImageIdList(ids);
        }

        boolean result = sharingWriteService.updateSharing(request, files);
        return ResponseEntity.ok(result);
    }


    @DeleteMapping("/{sharingId}")
    public ResponseEntity<?> deleteSharing(
            @PathVariable Long sharingId,
            @AuthenticationPrincipal MemberDetail memberDetail) throws IOException {
        Long memberId = memberDetail.getMemberResponse().getMemberId();
        boolean result = sharingWriteService.deleteSharing(sharingId, memberId);
        return ResponseEntity.ok(result);
    }


    @PostMapping("/{sharingId}/interest")
    public ResponseEntity<?> addInterest(
            @PathVariable Long sharingId,
            @AuthenticationPrincipal MemberDetail memberDetail) throws IOException {
        Long memberId = memberDetail.getMemberResponse().getMemberId();
        boolean result = sharingWriteService.addInterest(memberId, sharingId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{sharingId}/interest")
    public ResponseEntity<?> removeInterest(
            @PathVariable Long sharingId,
            @AuthenticationPrincipal MemberDetail memberDetail) throws IOException {
        Long memberId = memberDetail.getMemberResponse().getMemberId();
        boolean result = sharingWriteService.removeInterest(memberId, sharingId);
        return ResponseEntity.ok(result);
    }



    @PostMapping("/{sharingId}/comments")
    public ResponseEntity<?> addComment(
            @PathVariable Long sharingId,
            @RequestParam Long memberId,
            @RequestParam String content) {

        boolean result = sharingWriteService.addComment(sharingId, memberId, content);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<?> updateComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal MemberDetail memberDetail,
            @RequestBody CommentRequest request
    ) {
        request.setCommentId(commentId);
        request.setWriterId(memberDetail.getMemberResponse().getMemberId());

        boolean result = sharingWriteService.updateComment(request);
        return ResponseEntity.ok(result);
    }


    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal MemberDetail memberDetail,
            @RequestParam Long sharingId
    ) {
        CommentRequest request = new CommentRequest();
        request.setCommentId(commentId);
        request.setWriterId(memberDetail.getMemberResponse().getMemberId());
        request.setSharingId(sharingId);

        boolean result = sharingWriteService.deleteComment(request);
        return ResponseEntity.ok(result);
    }


}
