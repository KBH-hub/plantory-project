package com.zero.plantory.domain.plantingCalendar.mapper;

import com.zero.plantory.domain.plantingCalendar.vo.PlantingCalendarVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface PlantingCalendarMapper {
    int insertWatering(@Param("myplantId") Long myplantId);
    int updatePlantWateringCheck(@Param("wateringId") Long wateringId);
    int updateMyPlantWatering(@Param("myplantId") Long myplantId);
    int deletePlantWatering(@Param("myplantId") Long myplantId);
    List<PlantingCalendarVO> selectWateringCalendar(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    List<PlantingCalendarVO> selectDiaryCalendar(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
