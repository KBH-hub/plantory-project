package com.zero.plantory.domain.admin.reportManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportManagementPageResponse {
    private int totalCount;
    private List<ReportManagementResponse> list;
}
