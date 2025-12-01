package com.zero.plantory.domain.admin.reportManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportManagementSearchRequest {
    private String keyword;
    private String status;
    private Integer limit;
    private Integer offset;
}
