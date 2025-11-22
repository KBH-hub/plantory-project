package com.zero.plantory.domain.reportManagement.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportManagementSearchVO {
    private String keyword;
    private String status;
    private Integer limit;
    private Integer offset;
}
