package com.zero.plantory.domain.dashboard.mapper;

import com.zero.plantory.domain.dashboard.vo.RecommendedSharingVo;
import com.zero.plantory.domain.dashboard.vo.TodayDiaryVo;
import com.zero.plantory.domain.dashboard.vo.TodayWateringVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DashboardMapper {
    int countMyplantsByMemberId(Long memberId);
    int countTodayWatering(Long memberId);
    int countCareNeededPlants(Long memberId);
    List<RecommendedSharingVo> selectRecommendedSharing();
    List<TodayWateringVo> selectTodayWateringByMemberId(Long memberId);
    List<TodayDiaryVo> selectTodayDiaryByMemberId(Long memberId);
}
