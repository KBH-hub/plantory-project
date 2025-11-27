package com.zero.plantory.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MySharingHistoryListRequest {
    private Long memberId;
    private String myType;
    private String keyword;
    private String status;
    private Integer limit;
    private Integer offset;
}
