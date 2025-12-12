package com.zero.plantory.domain.admin.weightManagement.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RateResponse {
    BigDecimal initialSkillRate;
    BigDecimal skillRateGrade1;
    BigDecimal skillRateGrade2;
    BigDecimal skillRateGrade3;
    BigDecimal skillRateGrade4;

    BigDecimal initialManagementRate;
    BigDecimal managementRateGrade1;
    BigDecimal managementRateGrade2;
    BigDecimal managementRateGrade3;
}
