package com.zero.plantory.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyWrittenListRequest {
    private Long memberId;
    private String keyword;
    private int limit;
    private int offset;
}