package com.zero.plantory.domain.admin.weightManagement.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaveRateRequest {
    private Long memberId;

    private BigDecimal initialSkillRate;
    private BigDecimal skillRateGrade1;
    private BigDecimal skillRateGrade2;
    private BigDecimal skillRateGrade3;
    private BigDecimal skillRateGrade4;

    private BigDecimal initialManagementRate;
    private BigDecimal managementRateGrade1;
    private BigDecimal managementRateGrade2;
    private BigDecimal managementRateGrade3;
}
