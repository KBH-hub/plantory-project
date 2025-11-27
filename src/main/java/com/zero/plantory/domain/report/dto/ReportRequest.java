package com.zero.plantory.domain.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportRequest {
    private Long reportId;
    private Long adminId;
    private Long reporterId;
    private Long targetMemberId;
    private String content;
    private String status;
    private Date createdAt;
    private String adminMemo;
    private Date delFlag;
}
