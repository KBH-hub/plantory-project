package com.zero.plantory.domain.dashboard.mapper;

import com.zero.plantory.domain.dashboard.vo.RecommendedSharingVO;
import com.zero.plantory.domain.dashboard.vo.TodayDiaryVO;
import com.zero.plantory.domain.dashboard.vo.TodayWateringVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DashboardMapper {
    int countMyplantsByMemberId(Long memberId);
    int countTodayWatering(Long memberId);
    int countCareNeededPlants(Long memberId);
    List<RecommendedSharingVO> selectRecommendedSharing();
    List<TodayWateringVO> selectTodayWateringByMemberId(Long memberId);
    List<TodayDiaryVO> selectTodayDiaryByMemberId(Long memberId);
}
