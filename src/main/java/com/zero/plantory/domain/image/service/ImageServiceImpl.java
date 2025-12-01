package com.zero.plantory.domain.image.service;

import com.zero.plantory.domain.image.mapper.ImageMapper;
import com.zero.plantory.global.dto.ImageDTO;
import com.zero.plantory.global.dto.ImageTargetType;
import com.zero.plantory.global.utils.StorageUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageMapper imageMapper;
    private final StorageUploader storageUploader;

    @Transactional
    public String uploadProfileImage(Long memberId, MultipartFile file) throws IOException {

        // 1) 기존 프로필 사진 soft delete
        imageMapper.softDeleteImagesByTarget(ImageTargetType.PROFILE, memberId);

        // 2) GCS 업로드 --> URL 반환
        String fileUrl = storageUploader.uploadFile(file);

        // GCS에서 실제 저장된 파일명 추출
        String gcsFileName = extractFileNameFromUrl(fileUrl);

        // 3) DB insert
        ImageDTO dto = ImageDTO.builder()
                .memberId(memberId)
                .targetType(ImageTargetType.PROFILE)
                .targetId(memberId)
                .fileUrl(fileUrl)
                .fileName(gcsFileName)
                .build();

        imageMapper.insertImage(dto);

        return fileUrl;
    }

    @Override
    public ImageDTO getProfileImage(Long memberId) {
        return imageMapper.selectLatestProfileImage(ImageTargetType.PROFILE, memberId);
    }

    @Override
    public String getProfileImageUrl(Long memberId) {
        ImageDTO dto = imageMapper.selectLatestProfileImage(ImageTargetType.PROFILE, memberId);
        return dto != null ? dto.getFileUrl() : null;
    }



    private String extractFileNameFromUrl(String url) {
        int index = url.indexOf("/images/");
        return url.substring(index + 8);
    }
}
