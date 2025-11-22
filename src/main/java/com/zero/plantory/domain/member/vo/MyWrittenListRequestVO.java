package com.zero.plantory.domain.member.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyWrittenListRequestVO {
    private Long memberId;
    private String keyword;
    private int limit;
    private int offset;
}