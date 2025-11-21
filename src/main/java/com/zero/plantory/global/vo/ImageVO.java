package com.zero.plantory.global.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageVO {

    private Long imageId;
    private Long memberId;
    private ImageTargetType targetType;
    private Long targetId;
    private String fileUrl;
    private String fileName;
    private LocalDateTime createdAt;
    private LocalDateTime delFlag;
}