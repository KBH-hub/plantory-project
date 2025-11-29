package com.zero.plantory.domain.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileWrittenDeleteRequest {
    private Long memberId;
    private List<Long> sharingIds;
    private List<Long> questionIds;
}

