package com.zero.plantory.domain.sharing.service;

import com.zero.plantory.domain.image.ImageMapper;
import com.zero.plantory.domain.sharing.mapper.SharingMapper;
import com.zero.plantory.global.utils.StorageUploader;
import com.zero.plantory.global.vo.CommentVO;
import com.zero.plantory.global.vo.ImageTargetType;
import com.zero.plantory.global.vo.ImageVO;
import com.zero.plantory.global.vo.SharingVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SharingWriteServiceImpl implements SharingWriteService {

    private final SharingMapper sharingMapper;
    private final ImageMapper imageMapper;
    private final StorageUploader storageUploader;

    /** 글+ 이미지 등록 */
    @Override
    @Transactional
    public Long registerSharing(SharingVO vo, List<MultipartFile> files) throws IOException {

        List<ImageVO> imageList = new ArrayList<>();

        // 1) 이미지 먼저 업로드
        for (MultipartFile file : files) {
            String url = storageUploader.uploadFile(file);
            imageList.add(ImageVO.builder()
                    .memberId(vo.getMemberId())
                    .targetType(ImageTargetType.SHARING)
                    .targetId(null)              // 글 저장 후 세팅
                    .fileUrl(url)
                    .fileName(file.getOriginalFilename())
                    .build());
        }

        // 2) 글 저장
        sharingMapper.insertSharing(vo);
        Long sharingId = vo.getSharingId();

        // 3) 이미지 DB 저장
        for (ImageVO img : imageList) {
            img.setTargetId(sharingId);
            imageMapper.insertImage(img);
        }

        return sharingId;
    }


    /** 나눔글 수정 */
    @Override
    @Transactional
    public boolean updateSharing(SharingVO vo, List<MultipartFile> newImages) throws IOException {

        if (sharingMapper.countMySharing(vo.getSharingId(), vo.getMemberId()) == 0) {
            throw new IllegalStateException("본인 글만 수정 가능");
        }

        Long sharingId = vo.getSharingId();

        // 이미지를 변경하는 요청이 있는 경우에만 이미지 교체 수행
        if (newImages != null && !newImages.isEmpty()) {

            // 기존 이미지 soft delete
            imageMapper.softDeleteImagesByTarget(ImageTargetType.SHARING, sharingId);

            // 새 이미지 업로드 + DB저장
            for (MultipartFile file : newImages) {

                String url = storageUploader.uploadFile(file);

                imageMapper.insertImage(ImageVO.builder()
                        .memberId(vo.getMemberId())
                        .targetType(ImageTargetType.SHARING)
                        .targetId(sharingId)
                        .fileUrl(url)
                        .fileName(file.getOriginalFilename())
                        .build());
            }
        }

        return sharingMapper.updateSharing(vo) > 0;
    }


    /** 나눔글 삭제 */
    @Override
    @Transactional
    public boolean deleteSharing(Long sharingId, Long memberId) {

        int isMine = sharingMapper.countMySharing(sharingId, memberId);
        if (isMine == 0) {
            throw new IllegalStateException("본인 글만 삭제 가능");
        }

        int deleted = sharingMapper.deleteSharing(sharingId);
        imageMapper.softDeleteImagesByTarget(ImageTargetType.SHARING, sharingId);

        return deleted > 0;
    }

    /** 관심 등록 */
    @Override
    @Transactional
    public boolean addInterest(Long memberId, Long sharingId) {

        int exists = sharingMapper.countInterest(memberId, sharingId);
        if (exists > 0) return false;

        sharingMapper.insertInterest(memberId, sharingId);
        sharingMapper.increaseInterestNum(sharingId);

        return true;
    }

    /** 관심 해제 */
    @Override
    @Transactional
    public boolean removeInterest(Long memberId, Long sharingId) {

        int exists = sharingMapper.countInterest(memberId, sharingId);
        if (exists == 0) return false;

        sharingMapper.deleteInterest(memberId, sharingId);
        sharingMapper.decreaseInterestNum(sharingId);

        return true;
    }

    /** 댓글 등록 */
    @Override
    @Transactional
    public boolean addComment(Long sharingId, Long writerId, String content) {
        return sharingMapper.insertComment(sharingId, writerId, content) > 0;
    }

    /** 댓글 수정 */
    @Override
    @Transactional
    public boolean updateComment(CommentVO vo) {

        int isMine = sharingMapper.countMyComment(vo.getCommentId(), vo.getSharingId(), vo.getWriterId());
        if (isMine == 0) {
            throw new IllegalStateException("본인 댓글만 수정 가능");
        }

        return sharingMapper.updateCommentById(vo) > 0;
    }

    /** 댓글 삭제 */
    @Override
    @Transactional
    public boolean deleteComment(CommentVO vo) {

        int isMine = sharingMapper.countMyComment(vo.getCommentId(), vo.getSharingId(), vo.getWriterId());
        if (isMine == 0) {
            throw new IllegalStateException("본인 댓글만 삭제 가능");
        }

        return sharingMapper.deleteComment(vo) > 0;
    }


}
