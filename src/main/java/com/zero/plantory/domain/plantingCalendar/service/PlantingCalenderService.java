package com.zero.plantory.domain.plantingCalendar.service;

import com.zero.plantory.domain.plantingCalendar.vo.PlantingCalendarVO;
import com.zero.plantory.domain.plantingCalendar.vo.selectMyPlantDiaryVO;
import com.zero.plantory.global.vo.DiaryVO;
import com.zero.plantory.global.vo.ImageVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface PlantingCalenderService {
    int registerWatering(Long myplantId);
    int updatePlantWateringCheck(Long wateringId);
    int removePlantWatering(Long myplantId, Long removerId);
    List<PlantingCalendarVO> getWateringCalendar(Date startDate, Date endDate);
    List<PlantingCalendarVO> getDiaryCalendar(Date startDate, Date endDate);
    DiaryVO findDiaryUpdateInfo(Long diaryId);
    List<ImageVO> findDiaryUpdateImageInfo(Long diaryId);
    int updateDiary(DiaryVO vo, List<ImageVO> delImgList, List<MultipartFile> files, Long memberId) throws IOException;
    List<selectMyPlantDiaryVO> getMyPlant(Long memberId);
    int registerDiary(DiaryVO vo, List<MultipartFile> files, Long memberId) throws IOException;
}
