package com.zero.plantory.domain.message.dto;

import com.zero.plantory.global.dto.BoxType;
import com.zero.plantory.global.dto.TargetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageSearchRequest {
    private Long memberId;
    private BoxType boxType;
    private TargetType targetType;
    private String title;
    private int offset;
    private int limit;
}
