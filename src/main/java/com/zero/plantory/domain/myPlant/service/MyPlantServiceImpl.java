package com.zero.plantory.domain.myPlant.service;

import com.zero.plantory.domain.image.ImageMapper;
import com.zero.plantory.domain.myPlant.dto.MyPlantRequest;
import com.zero.plantory.domain.myPlant.dto.MyPlantResponse;
import com.zero.plantory.domain.myPlant.dto.MyPlantSearchNameResponse;
import com.zero.plantory.domain.myPlant.mapper.MyPlantMapper;
import com.zero.plantory.global.vo.ImageTargetType;
import com.zero.plantory.global.vo.ImageVO;
import com.zero.plantory.global.vo.MyPlantVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPlantServiceImpl implements MyPlantService {

    private final MyPlantMapper myPlantMapper;
    private final ImageMapper imageMapper;


    @Override
    public List<MyPlantResponse> getMyPlantList(Long memberId, int limit, int offset) {
        List<MyPlantResponse> resultList = new ArrayList<>();
        List<MyPlantVO> myPlantList = myPlantMapper.selectMyPlantList(memberId, limit, offset);
        for (MyPlantVO myPlantVO : myPlantList) {
            List<ImageVO> images = imageMapper.selectImagesByTarget(ImageTargetType.MYPLANT, myPlantVO.getMyplantId());
            String url = images.isEmpty() ? null : images.get(0).getFileUrl();
            MyPlantResponse dto = MyPlantResponse.builder()
                    .myplantId(myPlantVO.getMyplantId())
                    .name(myPlantVO.getName())
                    .startAt(myPlantVO.getStartAt())
                    .imageUrl(url)
                    .build();
            resultList.add(dto);
        }
        return resultList;
    }

    @Override
    public List<MyPlantResponse> getMyPlantByName(Long memberId, String name) {
        List<MyPlantResponse> resultList = new ArrayList<>();
        List<MyPlantSearchNameResponse> myPlantList = myPlantMapper.selectMyPlantByName(memberId, name);
        for (MyPlantSearchNameResponse response : myPlantList) {
            List<ImageVO> images = imageMapper.selectImagesByTarget(ImageTargetType.MYPLANT, response.getMyplantId());
            String url = images.isEmpty() ? null : images.get(0).getFileUrl();
            MyPlantResponse dto = MyPlantResponse.builder()
                    .myplantId(response.getMyplantId())
                    .name(response.getName())
                    .startAt(response.getStartAt())
                    .imageUrl(url)
                    .build();
            resultList.add(dto);
        }
        return resultList;
    }

    @Override
    public int registerMyPlant(MyPlantRequest request) {
        if(request.getName() == null || request.getName().equals("")){
            throw new IllegalArgumentException("내 식물 등록 필수값(식물 이름) 누락");
        }
        return myPlantMapper.insertMyPlant(request);
    }

    @Override
    public int updateMyPlant(MyPlantRequest request) {
        if(request.getName() == null || request.getName().equals("")){
            throw new IllegalArgumentException("내 식물 수정 필수값(식물 이름) 누락");
        }
        return myPlantMapper.updateMyPlant(request);
    }

    @Override
    public int removePlant(Long myplantId) {
        int result = myPlantMapper.deletePlant(myplantId);
        if(result == 0){
            throw new IllegalArgumentException("내 식물 삭제 실패");
        }
        return result;
    }
}
