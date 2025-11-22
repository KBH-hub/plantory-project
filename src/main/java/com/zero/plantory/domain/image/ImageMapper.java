package com.zero.plantory.domain.image;

import com.zero.plantory.global.vo.ImageVO;
import com.zero.plantory.global.vo.ImageTargetType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ImageMapper {

    // 단건 조회
    ImageVO selectImageById(@Param("imageId") Long imageId);
    // 다건 조회
    List<ImageVO> selectImagesByTarget(@Param("targetType") ImageTargetType targetType, @Param("targetId") Long targetId);

    int insertImage(ImageVO imageVO);
    int softDeleteImage(@Param("imageId") Long imageId);
    int softDeleteImagesByTarget(@Param("targetType") ImageTargetType targetType, @Param("targetId") Long targetId);
}
