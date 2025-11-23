package com.zero.plantory.domain.myPlant.service;

import com.zero.plantory.domain.myPlant.dto.MyPlantResponseDTO;
import com.zero.plantory.domain.myPlant.vo.MyPlantSearchVO;
import com.zero.plantory.global.vo.ImageVO;
import com.zero.plantory.global.vo.MyPlantVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MyPlantService {
    List<MyPlantResponseDTO> getMyPlantList(Long memberId, int limit, int offset);
    List<MyPlantResponseDTO> getMyPlantByName(Long memberId, String name);
    int registerMyPlant(MyPlantVO vo);
    int updateMyPlant(MyPlantVO vo);
    int removePlant(Long myplantId);
}
