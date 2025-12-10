package com.zero.plantory.domain.admin.weightManagement.mapper;

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

    void insertWeights(
            @Param("memberId") Long memberId,
            @Param("searchWeight") Double searchWeight,
            @Param("questionWeight") Double questionWeight
    );

    WeightLoggingResponse selectLatestWeights();

    List<Map<String, Object>> selectPlantsNeedingAttention();
}

