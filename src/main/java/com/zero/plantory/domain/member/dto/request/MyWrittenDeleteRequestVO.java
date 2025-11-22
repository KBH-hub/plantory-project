package com.zero.plantory.domain.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyWrittenDeleteRequestVO {
    private Long memberId;
    private List<Long> sharingIds;
    private List<Long> questionIds;
}

