package com.zero.plantory.domain.sharing.service;

import com.zero.plantory.global.vo.CommentVO;
import com.zero.plantory.global.vo.SharingVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SharingWriteService {
    /** 나눔글 등록 */
    Long registerSharing(SharingVO vo, List<MultipartFile> files) throws IOException;

    /** 나눔글 수정 */
    boolean updateSharing(SharingVO vo, List<MultipartFile> images) throws IOException;

    /** 나눔글 삭제 */
    boolean deleteSharing(Long sharingId, Long memberId);

    /** 관심 등록 */
    boolean addInterest(Long memberId, Long sharingId);

    /** 관심 해제 */
    boolean removeInterest(Long memberId, Long sharingId);

    /** 댓글 등록 */
    boolean addComment(Long sharingId, Long writerId, String content);

    /** 댓글 수정 */
    boolean updateComment(CommentVO vo);

    /** 댓글 삭제 */
    boolean deleteComment(CommentVO vo);

}
