package com.zero.plantory.domain.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SelectMyWrittenListRequestVO {
    private Long memberId;
    private String category;
    private String keyword;
    private int limit;
    private int offset;
}