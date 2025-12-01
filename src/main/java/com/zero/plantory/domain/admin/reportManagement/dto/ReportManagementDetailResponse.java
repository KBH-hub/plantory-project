package com.zero.plantory.domain.admin.reportManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportManagementDetailResponse {
    private Long reportId;
    private Long adminId;
    private Long reporterId;
    private Long targetMemberId;

    private String reporterName;
    private String targetName;
    private String adminName;

    private String content;
    private String status;
    private Date createdAt;
    private String adminMemo;
}
