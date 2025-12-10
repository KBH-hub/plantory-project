package com.zero.plantory.domain.admin.weightManagement.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class WeightLoggingResponse {
    private BigDecimal searchWeight;
    private BigDecimal questionWeight;
}
