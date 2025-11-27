package com.zero.plantory.domain.myPlant.mapper;

import com.zero.plantory.domain.myPlant.dto.MyPlantRequest;
import com.zero.plantory.domain.myPlant.dto.MyPlantSearchNameResponse;
import com.zero.plantory.global.vo.MyPlantVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MyPlantMapper {

    List<MyPlantVO> selectMyPlantList(@Param("memberId") Long memberId, @Param("limit") int limit, @Param("offset") int offset);
    List<MyPlantSearchNameResponse> selectMyPlantByName(@Param("memberId") Long memberId, @Param("name") String name);
    int insertMyPlant(MyPlantRequest vo);
    int updateMyPlant(MyPlantRequest vo);
    int deletePlant(Long myplantId);
}
