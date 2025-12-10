package com.zero.plantory.domain.admin.weightManagement.service;

import com.zero.plantory.domain.admin.weightManagement.dto.WeightLoggingResponse;
import com.zero.plantory.domain.admin.weightManagement.dto.WeightSaveRequest;
import com.zero.plantory.domain.admin.weightManagement.dto.WeightManagementResponse;

import java.util.List;
import java.util.Map;

public interface WeightManagementService {
    List<WeightManagementResponse> getMemberWeightList(String keyword, int limit, int offset, String range);
    void saveWeights(Long memberId,WeightSaveRequest req);
    WeightLoggingResponse getLatestWeights();
    Map<Long, Integer> getPlantsNeedingAttention();
}
