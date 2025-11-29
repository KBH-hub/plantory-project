package com.zero.plantory.domain.sharing.service;

import com.zero.plantory.domain.sharing.dto.CommentRequest;
import com.zero.plantory.domain.sharing.dto.SharingRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SharingWriteService {
    Long registerSharing(SharingRequest request, List<MultipartFile> files) throws IOException;

    boolean updateSharing(SharingRequest request, List<MultipartFile> images) throws IOException;

    boolean deleteSharing(Long sharingId, Long memberId);

    boolean addInterest(Long memberId, Long sharingId);

    boolean removeInterest(Long memberId, Long sharingId);

    boolean addComment(Long sharingId, Long writerId, String content);

    boolean updateComment(CommentRequest request);

    boolean deleteComment(CommentRequest request);

}
