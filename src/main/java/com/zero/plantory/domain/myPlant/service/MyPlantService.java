package com.zero.plantory.domain.myPlant.service;

import com.zero.plantory.domain.myPlant.dto.MyPlantRequest;
import com.zero.plantory.domain.myPlant.dto.MyPlantResponse;
import com.zero.plantory.global.dto.ImageDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MyPlantService {
    List<MyPlantResponse> getMyPlantList(Long memberId,String name ,int limit, int offset);
    List<MyPlantResponse> getMyPlantByName(Long memberId, String name);
    int registerMyPlant(MyPlantRequest vo, MultipartFile file, Long memberId) throws IOException;
    int updateMyPlant(MyPlantRequest vo, Long delFile, MultipartFile file, Long memberId) throws IOException;
    int removePlant(Long myplantId);
}
