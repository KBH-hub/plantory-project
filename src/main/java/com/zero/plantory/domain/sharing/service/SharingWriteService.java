package com.zero.plantory.domain.sharing.service;

import com.zero.plantory.domain.sharing.dto.CommentRequest;
import com.zero.plantory.domain.sharing.dto.SharingRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SharingWriteService {
    Long registerSharing(SharingRequest request, List<MultipartFile> files) throws IOException;

    /** 나눔글 수정 */
    boolean updateSharing(SharingRequest request, List<MultipartFile> images) throws IOException;

    /** 나눔글 삭제 */
    boolean deleteSharing(Long sharingId, Long memberId);

    /** 관심 등록 */
    boolean addInterest(Long memberId, Long sharingId);

    /** 관심 해제 */
    boolean removeInterest(Long memberId, Long sharingId);

    /** 댓글 등록 */
    boolean addComment(Long sharingId, Long writerId, String content);

    /** 댓글 수정 */
    boolean updateComment(CommentRequest request);

    /** 댓글 삭제 */
    boolean deleteComment(CommentRequest request);

}
