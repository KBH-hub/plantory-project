package com.zero.plantory.domain.plantingCalendar.mapper;

import com.zero.plantory.domain.plantingCalendar.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PlantingCalendarMapper {
    int updatePlantWateringCheck(@Param("wateringId") Long wateringId);
    int updateMyPlantWatering(@Param("myplantId") Long myplantId);
    int deletePlantWatering(@Param("myplantId") Long myplantId);
    List<PlantingCalendarResponse> selectWateringCalendar(@Param("memberId") Long memberId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    List<PlantingCalendarResponse> selectDiaryCalendar(@Param("memberId") Long memberId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    DiaryResponse selectDiaryUpdateInfo(@Param("diaryId") Long diaryId);
    int updateDiary(DiaryRequest vo);
    List<MyPlantDiaryResponse> selectMyPlant(@Param("memberId") Long memberId);
    int insertDiary(DiaryRequest vo);
    List<MyplantSlotBaseResponse> selectMyplantsForWindow(@Param("limit") int limit);
    int insertWateringAtIgnore(@Param("myplantId") Long myplantId, @Param("dateAt") java.time.LocalDateTime dateAt);

}
