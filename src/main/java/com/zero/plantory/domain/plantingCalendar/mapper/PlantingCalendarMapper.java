package com.zero.plantory.domain.plantingCalendar.mapper;

import com.zero.plantory.domain.plantingCalendar.vo.MyplantSlotBaseVO;
import com.zero.plantory.domain.plantingCalendar.vo.PlantingCalendarVO;
import com.zero.plantory.domain.plantingCalendar.vo.selectMyPlantDiaryVO;
import com.zero.plantory.global.vo.DiaryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PlantingCalendarMapper {
    int updatePlantWateringCheck(@Param("wateringId") Long wateringId);
    int updateMyPlantWatering(@Param("myplantId") Long myplantId);
    int deletePlantWatering(@Param("myplantId") Long myplantId);
    List<PlantingCalendarVO> selectWateringCalendar(@Param("memberId") Long memberId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    List<PlantingCalendarVO> selectDiaryCalendar(@Param("memberId") Long memberId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    DiaryVO selectDiaryUpdateInfo(@Param("diaryId") Long diaryId);
    int updateDiary(DiaryVO vo);
    List<selectMyPlantDiaryVO> selectMyPlant(@Param("memberId") Long memberId);
    int insertDiary(DiaryVO vo);
    List<MyplantSlotBaseVO> selectMyplantsForWindow(@Param("limit") int limit);
    int insertWateringAtIgnore(@Param("myplantId") Long myplantId, @Param("dateAt") java.time.LocalDateTime dateAt);

}
