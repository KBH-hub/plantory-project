package com.zero.plantory.domain.myPlant.service;

import com.zero.plantory.domain.myPlant.dto.MyPlantRequest;
import com.zero.plantory.domain.myPlant.dto.MyPlantResponse;

import java.util.List;

public interface MyPlantService {
    List<MyPlantResponse> getMyPlantList(Long memberId,String name ,int limit, int offset);
    List<MyPlantResponse> getMyPlantByName(Long memberId, String name);
    int registerMyPlant(MyPlantRequest vo);
    int updateMyPlant(MyPlantRequest vo);
    int removePlant(Long myplantId);
}
