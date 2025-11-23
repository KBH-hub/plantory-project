package com.zero.plantory.domain.myPlant.service;

import com.zero.plantory.domain.image.ImageMapper;
import com.zero.plantory.domain.myPlant.dto.MyPlantResponseDTO;
import com.zero.plantory.domain.myPlant.mapper.MyPlantMapper;
import com.zero.plantory.domain.myPlant.vo.MyPlantSearchVO;
import com.zero.plantory.global.vo.ImageTargetType;
import com.zero.plantory.global.vo.ImageVO;
import com.zero.plantory.global.vo.MyPlantVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPlantServiceImpl implements MyPlantService {

    private final MyPlantMapper myPlantMapper;
    private final ImageMapper imageMapper;


    @Override
    public List<MyPlantResponseDTO> getMyPlantList(Long memberId, int limit, int offset) {
        List<MyPlantResponseDTO> resultList = new ArrayList<>();
        List<MyPlantVO> myPlantList = myPlantMapper.selectMyPlantList(memberId, limit, offset);
        for (MyPlantVO myPlantVO : myPlantList) {
            List<ImageVO> images = imageMapper.selectImagesByTarget(ImageTargetType.MYPLANT, myPlantVO.getMyplantId());
            String url = images.isEmpty() ? null : images.get(0).getFileUrl();
            MyPlantResponseDTO dto = MyPlantResponseDTO.builder()
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
    public List<MyPlantResponseDTO> getMyPlantByName(Long memberId, String name) {
        List<MyPlantResponseDTO> resultList = new ArrayList<>();
        List<MyPlantSearchVO> myPlantList = myPlantMapper.selectMyPlantByName(memberId, name);
        for (MyPlantSearchVO myPlantVO : myPlantList) {
            List<ImageVO> images = imageMapper.selectImagesByTarget(ImageTargetType.MYPLANT, myPlantVO.getMyplantId());
            String url = images.isEmpty() ? null : images.get(0).getFileUrl();
            MyPlantResponseDTO dto = MyPlantResponseDTO.builder()
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
    public int registerMyPlant(MyPlantVO vo) {
        if(vo.getName() == null || vo.getName().equals("")){
            throw new IllegalArgumentException("내 식물 등록 필수값(식물 이름) 누락");
        }
        return myPlantMapper.insertMyPlant(vo);
    }

    @Override
    public int updateMyPlant(MyPlantVO vo) {
        if(vo.getName() == null || vo.getName().equals("")){
            throw new IllegalArgumentException("내 식물 수정 필수값(식물 이름) 누락");
        }
        return myPlantMapper.updateMyPlant(vo);
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
