package com.zero.plantory.domain.admin.weightManagement.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WeightManagementResponse {
    private Long memberId;
    private String membername;
    private String nickname;
    private Integer searchWeight;
    private Integer questionWeight;
    private Integer plantsNeedingAttention;
    private Long totalCount;
}
