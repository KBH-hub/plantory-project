package com.zero.plantory.global.utils;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.zero.plantory.domain.image.dto.UploadResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GcsUploader implements StorageUploader {
    private final Storage storage;
    private final String bucketName = "plantory";

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String fileName = String.format("images/%s/%s-%s",
                today,
                UUID.randomUUID(),
                file.getOriginalFilename()
        );

        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, fileName)
                .setContentType(file.getContentType())
                .build();

        storage.create(blobInfo, file.getBytes());

        return String.format("https://storage.googleapis.com/%s/%s", bucketName, fileName);
    }

    @Override
    public boolean deleteFile(String fileName) {
        return storage.delete(bucketName, fileName);
    }
}
