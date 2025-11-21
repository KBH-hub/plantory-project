package com.zero.plantory.domain.myPlant.mapper;

import com.zero.plantory.global.vo.MyPlantVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MyPlantMapper {
    List<MyPlantVO> selectMyPlantList(@Param("memberId") Long memberId, @Param("limit") int limit, @Param("offset") int offset);

}
