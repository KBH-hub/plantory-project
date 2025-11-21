package com.zero.plantory.domain.plantingCalendar.mapper;

import com.zero.plantory.domain.plantingCalendar.vo.PlantingCalendarVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PlantingCalendarMapper {
    int insertWatering(@Param("myplantId") Long myplantId);
    int updatePlantWateringCheck(@Param("wateringId") Long wateringId);
    int updateMyPlantWatering(@Param("myplantId") Long myplantId);
    int deletePlantWatering(@Param("myplantId") Long myplantId);
}
