package com.zero.plantory.domain.myPlant.service;

import com.zero.plantory.domain.image.mapper.ImageMapper;
import com.zero.plantory.domain.myPlant.dto.MyPlantRequest;
import com.zero.plantory.domain.myPlant.dto.MyPlantResponse;
import com.zero.plantory.domain.myPlant.dto.MyPlantSearchNameResponse;
import com.zero.plantory.domain.myPlant.mapper.MyPlantMapper;
import com.zero.plantory.global.dto.ImageTargetType;
import com.zero.plantory.global.dto.ImageDTO;
import com.zero.plantory.global.utils.StorageUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPlantServiceImpl implements MyPlantService {

    private final MyPlantMapper myPlantMapper;
    private final ImageMapper imageMapper;
    private final StorageUploader storageUploader;

    @Override
    public List<MyPlantResponse> getMyPlantList(Long memberId, String name, int limit, int offset) {
        List<MyPlantResponse> resultList = new ArrayList<>();
        List<MyPlantResponse> myPlantList = myPlantMapper.selectMyPlantList(memberId, name, limit, offset);
        for (MyPlantResponse response : myPlantList) {
            List<ImageDTO> images = imageMapper.selectImagesByTarget(ImageTargetType.MYPLANT, response.getMyplantId());
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
    public List<MyPlantResponse> getMyPlantByName(Long memberId, String name) {
        List<MyPlantResponse> resultList = new ArrayList<>();
        List<MyPlantSearchNameResponse> myPlantList = myPlantMapper.selectMyPlantByName(memberId, name);
        for (MyPlantSearchNameResponse response : myPlantList) {
            List<ImageDTO> images = imageMapper.selectImagesByTarget(ImageTargetType.MYPLANT, response.getMyplantId());
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
    @Transactional
    public int registerMyPlant(MyPlantRequest request, MultipartFile file, Long memberId) throws IOException {
        if (request.getName() == null || request.getName().equals("")) {
            throw new IllegalArgumentException("내 식물 등록 필수값(식물 이름) 누락");
        }

        int insertMyplant = myPlantMapper.insertMyPlant(request);
        if (insertMyplant == 0) {
            throw new IllegalStateException("관찰일지 저장 실패");
        }

        Long myplantId = request.getMyplantId();
        if (myplantId == null) {
            throw new IllegalStateException("myplantId 미할당");
        }

        int insertedImages = 0;
        String url = storageUploader.uploadFile(file);

        ImageDTO image = ImageDTO.builder()
                .memberId(memberId)
                .targetType(ImageTargetType.MYPLANT)
                .targetId(myplantId)
                .fileUrl(url)
                .fileName(file.getOriginalFilename())
                .build();

        insertedImages += imageMapper.insertImage(image);

        if (insertMyplant == 1) {
            if (image != null) {
                if (insertedImages == 1) {
                    return 2;
                }
            }
            return 1;
        }

        throw new IllegalArgumentException("내 식물 등록 실패");
    }

    @Override
    @Transactional
    public int updateMyPlant(MyPlantRequest request, Long delFileTargetId, MultipartFile file, Long memberId) throws IOException {
        int result = 0;
        result += myPlantMapper.updateMyPlant(request);
        int fileCount = 0;
        fileCount += delFileTargetId == null ? 0 : 1;
        fileCount += file == null ? 0 : 1;

        if (request.getName() == null || request.getName().equals("")) {
            throw new IllegalArgumentException("내 식물 수정 필수값(식물 이름) 누락");
        }

        if(delFileTargetId != null) {
            result += imageMapper.softDeleteImagesByTarget(ImageTargetType.MYPLANT, delFileTargetId);
        }

        if(file != null) {
            String url = storageUploader.uploadFile(file);

            ImageDTO image = ImageDTO.builder()
                    .memberId(memberId)
                    .targetType(ImageTargetType.MYPLANT)
                    .targetId(request.getMyplantId())
                    .fileUrl(url)
                    .fileName(file.getOriginalFilename())
                    .build();
            result += imageMapper.insertImage(image);
        }

        if (result == fileCount + 1) {
            return result;
        }
        throw new IllegalStateException("관찰일지 수정 실패(업데이트 누락)");
    }

    @Override
    public int removePlant(Long myplantId) {
        int result = myPlantMapper.deletePlant(myplantId);
        if (result == 0) {
            throw new IllegalArgumentException("내 식물 삭제 실패");
        }
        return result;
    }
}
