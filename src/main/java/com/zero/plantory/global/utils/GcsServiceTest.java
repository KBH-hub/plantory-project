package com.zero.plantory.global.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class GcsServiceTest {
    private final StorageUploader storageUploader;

    public String uploadImage(MultipartFile file) throws IOException {
        return storageUploader.uploadFile(file);
    }

    public boolean deleteImage(String fileName) {
        return storageUploader.deleteFile(fileName);
    }
}
