package com.zero.plantory.global.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageUploader {
    String uploadFile(MultipartFile file) throws IOException;
    boolean deleteFile(String fileName);
}
