package com.zero.plantory.domain.admin.weightManagement.service;

import com.zero.plantory.domain.admin.weightManagement.dto.SaveRateRequest;
import com.zero.plantory.domain.admin.weightManagement.dto.RateResponse;
import com.zero.plantory.domain.admin.weightManagement.dto.WeightLoggingResponse;
import com.zero.plantory.domain.admin.weightManagement.dto.SaveWeightRequest;
import com.zero.plantory.domain.admin.weightManagement.dto.WeightManagementResponse;

import java.util.List;
import java.util.Map;

public interface WeightManagementService {
    List<WeightManagementResponse> getMemberWeightList(String keyword, int limit, int offset, String range);
    void saveWeights(Long memberId, SaveWeightRequest saveWeightRequest);
    WeightLoggingResponse getLatestWeights();
    Map<Long, Integer> getPlantsNeedingAttention();
    RateResponse getRate();
    void saveRate(Long memberId, SaveRateRequest saveRateRequest);
}