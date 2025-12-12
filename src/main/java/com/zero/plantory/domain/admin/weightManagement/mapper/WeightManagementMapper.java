package com.zero.plantory.domain.admin.weightManagement.mapper;

import com.zero.plantory.domain.admin.weightManagement.dto.SaveRateRequest;
import com.zero.plantory.domain.admin.weightManagement.dto.RateResponse;
import com.zero.plantory.domain.admin.weightManagement.dto.SaveWeightRequest;
import com.zero.plantory.domain.admin.weightManagement.dto.WeightLoggingResponse;
import com.zero.plantory.domain.admin.weightManagement.dto.WeightManagementResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface WeightManagementMapper {

    List<WeightManagementResponse> selectWeightManagementList(
            @Param("keyword") String keyword,
            @Param("limit") Integer limit,
            @Param("offset") Integer offset,
            @Param("startDate") LocalDateTime startDate
    );

    int insertWeights(SaveWeightRequest  saveWeightRequest);

    WeightLoggingResponse selectLatestWeights();

    List<Map<String, Object>> selectPlantsNeedingAttention();

    RateResponse selectLatestRateGrades();

    int insertSkillRateGrade(SaveRateRequest saveRateRequest);
    int insertManagementRateGrade(SaveRateRequest saveRateRequest);
}

