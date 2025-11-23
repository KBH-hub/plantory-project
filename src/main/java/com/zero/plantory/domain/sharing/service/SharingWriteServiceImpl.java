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

    @Override
    @Transactional
    public Long registerSharing(SharingVO vo, List<MultipartFile> files) throws IOException {

        List<ImageVO> imageList = new ArrayList<>();

        for (MultipartFile file : files) {
            String url = storageUploader.uploadFile(file);
            imageList.add(ImageVO.builder()
                    .memberId(vo.getMemberId())
                    .targetType(ImageTargetType.SHARING)
                    .targetId(null)
                    .fileUrl(url)
                    .fileName(file.getOriginalFilename())
                    .build());
        }

        sharingMapper.insertSharing(vo);
        Long sharingId = vo.getSharingId();

        for (ImageVO img : imageList) {
            img.setTargetId(sharingId);
            imageMapper.insertImage(img);
        }

        return sharingId;
    }


    @Override
    @Transactional
    public boolean updateSharing(SharingVO vo, List<MultipartFile> newImages) throws IOException {

        if (sharingMapper.countMySharing(vo.getSharingId(), vo.getMemberId()) == 0) {
            throw new IllegalStateException("본인 글만 수정 가능");
        }

        Long sharingId = vo.getSharingId();

        if (newImages != null && !newImages.isEmpty()) {

            imageMapper.softDeleteImagesByTarget(ImageTargetType.SHARING, sharingId);

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
        return sharingMapper.insertComment(sharingId, writerId, content) > 0;
    }

    @Override
    @Transactional
    public boolean updateComment(CommentVO vo) {

        int isMine = sharingMapper.countMyComment(vo.getCommentId(), vo.getSharingId(), vo.getWriterId());
        if (isMine == 0) {
            throw new IllegalStateException("본인 댓글만 수정 가능");
        }

        return sharingMapper.updateCommentById(vo) > 0;
    }

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
