package com.zero.plantory.domain.image;

import com.zero.plantory.global.dto.ImageTargetType;
import com.zero.plantory.global.dto.ImageDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class ImageMapperTest {

    @Autowired
    private ImageMapper mapper;

    @Test
    @DisplayName("단건 이미지 조회")
    void selectImageByIdTest() {
        log.info("단건 이미지 조회 결과 = {}", mapper.selectImageById(1L));
    }

    @Test
    @DisplayName("다건 이미지 조회")
    void selectImagesByTargetTest() {
        log.info("이미지 리스트 = {}", mapper.selectImagesByTarget(ImageTargetType.QUESTION, 2L));
    }

    @Test
    @DisplayName("이미지 insert")
    void insertImageTest() {
        ImageDTO vo = ImageDTO.builder()
                .memberId(1L)
                .targetType(ImageTargetType.QUESTION)
                .targetId(2L)
                .fileUrl("https://storage.googleapis.com/plantory/sample.jpg")
                .fileName("sample.jpg")
                .build();

        log.info("Insert 결과 = {}", mapper.insertImage(vo));
    }

    @Test
    @DisplayName("단건 이미지 soft delete")
    void softDeleteImageTest() {
        log.info("softDeleteImage 결과 = {}", mapper.softDeleteImage(1L));
    }

    @Test
    @DisplayName("특정 게시글 이미지 전체 soft delete")
    void softDeleteImagesByTargetTest() {
        log.info("softDeleteImagesByTarget 결과 = {}", mapper.softDeleteImagesByTarget(ImageTargetType.QUESTION, 2L));
    }
}
