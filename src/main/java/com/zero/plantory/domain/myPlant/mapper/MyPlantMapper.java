package com.zero.plantory.domain.myPlant.mapper;

import com.zero.plantory.domain.myPlant.vo.MyPlantSearchVO;
import com.zero.plantory.global.vo.MyPlantVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MyPlantMapper {

    List<MyPlantVO> selectMyPlantList(@Param("memberId") Long memberId, @Param("limit") int limit, @Param("offset") int offset);
    List<MyPlantSearchVO> selectMyPlantByName(@Param("memberId") Long memberId, @Param("name") String name);
    int insertMyPlant(MyPlantVO vo);
    int updateMyPlant(MyPlantVO vo);
    int deletePlant(Long myplantId);
}
