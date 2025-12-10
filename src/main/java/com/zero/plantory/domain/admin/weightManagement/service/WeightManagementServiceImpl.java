package com.zero.plantory.domain.admin.weightManagement.service;


import com.zero.plantory.domain.admin.weightManagement.dto.WeightLoggingResponse;
import com.zero.plantory.domain.admin.weightManagement.dto.WeightSaveRequest;
import com.zero.plantory.domain.admin.weightManagement.dto.WeightManagementResponse;
import com.zero.plantory.domain.admin.weightManagement.mapper.WeightManagementMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeightManagementServiceImpl implements WeightManagementService  {

    private final WeightManagementMapper weightManagementMapper;

    public List<WeightManagementResponse> getMemberWeightList(String keyword, int limit, int offset, String range) {
        LocalDateTime startDate = null;

        if (range != null && !range.equals("ALL")) {
            int days = Integer.parseInt(range.replace("D", ""));
            startDate = LocalDateTime.now().minusDays(days);
        }
        return weightManagementMapper.selectWeightManagementList(keyword,limit,offset,startDate);
    }

    public void saveWeights(Long memberId,WeightSaveRequest req) {
        weightManagementMapper.insertWeights(
                memberId,
                req.getSearchWeight(),
                req.getQuestionWeight()
        );
    }

    public WeightLoggingResponse getLatestWeights() {
        return weightManagementMapper.selectLatestWeights();
    }

    @Override
    public Map<Long, Integer> getPlantsNeedingAttention() {
        List<Map<String, Object>> list = weightManagementMapper.selectPlantsNeedingAttention();

        return list.stream()
                .collect(Collectors.toMap(
                        row -> ((Number) row.get("member_id")).longValue(),
                        row -> ((Number) row.get("care_count")).intValue()
                ));
    }


}
