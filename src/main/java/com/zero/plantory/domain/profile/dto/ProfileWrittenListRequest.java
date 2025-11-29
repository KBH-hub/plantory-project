package com.zero.plantory.domain.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileWrittenListRequest {
    private Long memberId;
    private String keyword;
    private int limit;
    private int offset;
}