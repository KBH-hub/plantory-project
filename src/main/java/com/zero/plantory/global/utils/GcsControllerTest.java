package com.zero.plantory.global.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class GcsControllerTest {
    private final GcsServiceTest gcsService;

    @PostMapping
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        String url = gcsService.uploadImage(file);
        return ResponseEntity.ok(Map.of("url", url));
    }

    @DeleteMapping
    public ResponseEntity<Map<String, String>> deleteImage(@RequestParam("fileName") String fileName) {
        boolean result = gcsService.deleteImage(fileName);
        if (result) {
            return ResponseEntity.ok(Map.of("message", "삭제 완료"));
        } else {
            return ResponseEntity.status(404).body(Map.of("message", "파일을 찾을 수 없습니다."));
        }
    }
}
