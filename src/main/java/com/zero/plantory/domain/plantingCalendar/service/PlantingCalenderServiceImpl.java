package com.zero.plantory.domain.plantingCalendar.service;

import com.zero.plantory.domain.image.ImageMapper;
import com.zero.plantory.domain.plantingCalendar.mapper.PlantingCalendarMapper;
import com.zero.plantory.domain.plantingCalendar.vo.PlantingCalendarVO;
import com.zero.plantory.domain.plantingCalendar.vo.selectMyPlantDiaryVO;
import com.zero.plantory.domain.report.mapper.ReportMapper;
import com.zero.plantory.global.utils.StorageUploader;
import com.zero.plantory.global.vo.DiaryVO;
import com.zero.plantory.global.vo.ImageTargetType;
import com.zero.plantory.global.vo.ImageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PlantingCalenderServiceImpl implements PlantingCalenderService {
    private final PlantingCalendarMapper plantingCalendarMapper;
    private final ImageMapper imageMapper;
    private final StorageUploader storageUploader;

    @Override
    public int registerWatering(Long myplantId) {
        return plantingCalendarMapper.insertWatering(myplantId);
    }

    @Override
    public int updatePlantWateringCheck(Long wateringId) {
        return plantingCalendarMapper.updatePlantWateringCheck(wateringId);
    }

    @Override
    @Transactional
    public int removePlantWatering(Long myplantId, Long removerId) {
        List<selectMyPlantDiaryVO> list = plantingCalendarMapper.selectMyPlant(removerId);
        for (selectMyPlantDiaryVO myPlant : list) {
            if (Objects.equals(myPlant.getMyplantId(), myplantId)) {
                plantingCalendarMapper.updateMyPlantWatering(myplantId);
                plantingCalendarMapper.deletePlantWatering(myplantId);
            }
            return 1;
        }
        throw new IllegalStateException("물주기 삭제 실패(식물 소유자 미일치)");
    }

    @Override
    public List<PlantingCalendarVO> getWateringCalendar(Long memberId, LocalDateTime startDate, LocalDateTime endDate) {
        return plantingCalendarMapper.selectWateringCalendar(memberId, startDate, endDate);
    }

    @Override
    public List<PlantingCalendarVO> getDiaryCalendar(Long memberId, LocalDateTime startDate, LocalDateTime endDate) {
        return plantingCalendarMapper.selectDiaryCalendar(memberId, startDate, endDate);
    }

    @Override
    public DiaryVO findDiaryUpdateInfo(Long diaryId) {
        return plantingCalendarMapper.selectDiaryUpdateInfo(diaryId);
    }

    @Override
    public List<ImageVO> findDiaryUpdateImageInfo(Long diaryId) {
        return imageMapper.selectImagesByTarget(ImageTargetType.DIARY, diaryId);
    }

    @Override
    @Transactional
    public int updateDiary(DiaryVO diaryVO, List<ImageVO> delImgList, List<MultipartFile> files, Long memberId) throws IOException {
        int result = 0;
        result += plantingCalendarMapper.updateDiary(diaryVO);

        for (ImageVO image : delImgList) {
            result += imageMapper.softDeleteImagesByTarget(ImageTargetType.DIARY, image.getImageId());
        }

        for(MultipartFile file:files){
            String url = storageUploader.uploadFile(file);

            ImageVO image = ImageVO.builder()
                    .memberId(memberId)
                    .targetType(ImageTargetType.DIARY)
                    .targetId(diaryVO.getDiaryId())
                    .fileUrl(url)
                    .fileName(file.getOriginalFilename())
                    .build();
            result += imageMapper.insertImage(image);

        }

        if(result == delImgList.size()+files.size()+1){
            return result;
        }
        throw new IllegalStateException("관찰일지 수정 실패(업데이트 누락)");
    }

    @Override
    public List<selectMyPlantDiaryVO> getMyPlant(Long memberId) {
        return plantingCalendarMapper.selectMyPlant(memberId);
    }

    @Override
    @Transactional
    public int registerDiary(DiaryVO diaryVO, List<MultipartFile> files, Long memberId) throws IOException {
        int result = 0;
        result += plantingCalendarMapper.insertDiary(diaryVO);

        for(MultipartFile file:files){
            String url = storageUploader.uploadFile(file);

            ImageVO image = ImageVO.builder()
                    .memberId(memberId)
                    .targetType(ImageTargetType.DIARY)
                    .targetId(diaryVO.getDiaryId())
                    .fileUrl(url)
                    .fileName(file.getOriginalFilename())
                    .build();
            result += imageMapper.insertImage(image);

        }
        if(result == files.size()+1){
            return result;
        }
        throw new IllegalStateException("관찰일지 등록 실패(업데이트 누락)");
    }

    @Transactional
    public int processOnce(int batchSize) {
        var rows = plantingCalendarMapper.selectDueWateringWithNextAt(batchSize);
        int ok = 0;
        for (var r : rows) {
            // 중복 방지(같은 nextAt에 이미 기록이 있으면 skip) — DDL 변경 없이 애플리케이션 체크
            // 필요하면 mapper에 existsWatering(myplantId, dateAt) 추가
            plantingCalendarMapper.insertWateringAt(r.getMyplantId(), r.getNextAt());
            ok++;
        }
        return ok;
    }
}
