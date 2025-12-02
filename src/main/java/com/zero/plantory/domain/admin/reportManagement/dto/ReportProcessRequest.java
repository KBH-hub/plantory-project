package com.zero.plantory.domain.admin.reportManagement.dto;

import lombok.Data;

@Data
public class ReportProcessRequest {
    private Long targetMemberId;
    private String adminMemo;
    private int stopDays;
}

