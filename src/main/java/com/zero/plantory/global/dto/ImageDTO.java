package com.zero.plantory.global.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageDTO {

    private Long imageId;
    private Long memberId;
    private ImageTargetType targetType;
    private Long targetId;
    private String fileUrl;
    private String fileName;
    private LocalDateTime createdAt;
    private LocalDateTime delFlag;
}