package com.zero.plantory.domain.image.service;

import com.zero.plantory.global.dto.ImageDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    String uploadProfileImage(Long memberId, MultipartFile file) throws IOException;
    ImageDTO getProfileImage(Long memberId);
    String getProfileImageUrl(Long memberId);
}
