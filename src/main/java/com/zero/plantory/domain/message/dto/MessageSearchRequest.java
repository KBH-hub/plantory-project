package com.zero.plantory.domain.message.dto;

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
    private String boxType;
    private String targetType;
    private String title;
    private int offset;
    private int limit;
}
