package com.zero.plantory.domain.admin.reportManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportManagementResponse {
    private Long reportId;
    private Long adminId;
    private Long reporterId;
    private Long targetMemberId;
    private String content;
    private String status;
    private LocalDateTime createdAt;
    private String adminMemo;
    private LocalDateTime delFlag;
}
