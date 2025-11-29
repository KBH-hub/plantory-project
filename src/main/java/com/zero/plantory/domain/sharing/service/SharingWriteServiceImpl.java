package com.zero.plantory.domain.sharing.service;

import com.zero.plantory.domain.image.ImageMapper;
import com.zero.plantory.domain.notice.NoticeMapper;
import com.zero.plantory.domain.sharing.dto.CommentRequest;
import com.zero.plantory.domain.sharing.dto.SelectSharingDetailResponse;
import com.zero.plantory.domain.sharing.dto.SharingRequest;
import com.zero.plantory.domain.sharing.mapper.SharingMapper;
import com.zero.plantory.global.utils.StorageUploader;
import com.zero.plantory.global.dto.ImageTargetType;
import com.zero.plantory.global.dto.ImageDTO;
import com.zero.plantory.global.dto.NoticeTargetType;
import com.zero.plantory.global.dto.NoticeDTO;
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
    private final NoticeMapper noticeMapper;

    @Override
    @Transactional
    public Long registerSharing(SharingRequest request, List<MultipartFile> files) throws IOException {

        List<ImageDTO> imageList = new ArrayList<>();

        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new IllegalArgumentException("제목은 필수입니다.");
        }

        if (files != null) {
            for (MultipartFile file : files) {

                if (file.isEmpty()) continue;

                String url = storageUploader.uploadFile(file);

                imageList.add(ImageDTO.builder()
                        .memberId(request.getMemberId())
                        .targetType(ImageTargetType.SHARING)
                        .fileUrl(url)
                        .fileName(file.getOriginalFilename())
                        .build());
            }
        }

        sharingMapper.insertSharing(request);
        Long sharingId = request.getSharingId();

        for (ImageDTO img : imageList) {
            img.setTargetId(sharingId);
            imageMapper.insertImage(img);
        }

        return sharingId;
    }


    @Override
    @Transactional
    public boolean updateSharing(SharingRequest request, List<MultipartFile> newImages) throws IOException {

        if (sharingMapper.countProfileSharing(request.getSharingId(), request.getMemberId()) == 0) {
            throw new IllegalStateException("본인 글만 수정 가능");
        }

        Long sharingId = request.getSharingId();

        if (newImages != null && !newImages.isEmpty()) {

            imageMapper.softDeleteImagesByTarget(ImageTargetType.SHARING, sharingId);

            for (MultipartFile file : newImages) {

                String url = storageUploader.uploadFile(file);

                imageMapper.insertImage(ImageDTO.builder()
                        .memberId(request.getMemberId())
                        .targetType(ImageTargetType.SHARING)
                        .targetId(sharingId)
                        .fileUrl(url)
                        .fileName(file.getOriginalFilename())
                        .build());
            }
        }

        return sharingMapper.updateSharing(request) > 0;
    }


    @Override
    @Transactional
    public boolean deleteSharing(Long sharingId, Long memberId) {

        int isMine = sharingMapper.countProfileSharing(sharingId, memberId);
        if (isMine == 0) {
            throw new IllegalStateException("본인 글만 삭제 가능");
        }

        int deleted = sharingMapper.deleteSharing(sharingId);
        imageMapper.softDeleteImagesByTarget(ImageTargetType.SHARING, sharingId);

        return deleted > 0;
    }

    @Override
    @Transactional
    public boolean addInterest(Long memberId, Long sharingId) {

        int exists = sharingMapper.countInterest(memberId, sharingId);
        if (exists > 0) return false;

        sharingMapper.insertInterest(memberId, sharingId);
        sharingMapper.increaseInterestNum(sharingId);

        return true;
    }

    @Override
    @Transactional
    public boolean removeInterest(Long memberId, Long sharingId) {

        int exists = sharingMapper.countInterest(memberId, sharingId);
        if (exists == 0) return false;

        sharingMapper.deleteInterest(memberId, sharingId);
        sharingMapper.decreaseInterestNum(sharingId);

        return true;
    }

    @Override
    @Transactional
    public boolean addComment(Long sharingId, Long writerId, String content) {
        SelectSharingDetailResponse sharing = sharingMapper.selectSharingDetail(sharingId);
        int inserted = sharingMapper.insertComment(sharingId, writerId, content);

        if (inserted > 0) {

            Long ownerId = sharing.getMemberId();

            if (!writerId.equals(ownerId)) {

                NoticeDTO notice = NoticeDTO.builder()
                        .receiverId(ownerId)
                        .targetId(sharingId)
                        .targetType(NoticeTargetType.SHARING)
                        .content("새로운 댓글이 등록되었습니다!")
                        .build();

                noticeMapper.insertNotice(notice);
            }
        }

        return inserted > 0;
    }

    @Override
    @Transactional
    public boolean updateComment(CommentRequest request) {

        int isMine = sharingMapper.countProfileComment(request.getCommentId(), request.getSharingId(), request.getWriterId());
        if (isMine == 0) {
            throw new IllegalStateException("본인 댓글만 수정 가능");
        }

        return sharingMapper.updateCommentById(request) > 0;
    }

    @Override
    @Transactional
    public boolean deleteComment(CommentRequest request) {

        int isMine = sharingMapper.countProfileComment(request.getCommentId(), request.getSharingId(), request.getWriterId());
        if (isMine == 0) {
            throw new IllegalStateException("본인 댓글만 삭제 가능");
        }

        return sharingMapper.deleteComment(request) > 0;
    }


}
