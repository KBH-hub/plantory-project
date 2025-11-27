package com.zero.plantory.domain.plantingCalendar.service;

import com.zero.plantory.domain.plantingCalendar.dto.DiaryRequest;
import com.zero.plantory.domain.plantingCalendar.dto.DiaryResponse;
import com.zero.plantory.domain.plantingCalendar.dto.MyPlantDiaryResponse;
import com.zero.plantory.domain.plantingCalendar.dto.PlantingCalendarResponse;
import com.zero.plantory.global.dto.ImageDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface PlantingCalenderService {
    int updatePlantWateringCheck(Long wateringId);
    int removePlantWatering(Long myplantId, Long removerId);
    List<PlantingCalendarResponse> getWateringCalendar(Long memberId, LocalDateTime startDate, LocalDateTime endDate);
    List<PlantingCalendarResponse> getDiaryCalendar(Long memberId, LocalDateTime startDate, LocalDateTime endDate);
    DiaryResponse findDiaryUpdateInfo(Long diaryId);
    List<ImageDTO> findDiaryUpdateImageInfo(Long diaryId);
    int updateDiary(DiaryRequest request, List<ImageDTO> delImgList, List<MultipartFile> files, Long memberId) throws IOException;
    List<MyPlantDiaryResponse> getMyPlant(Long memberId);
    int registerDiary(DiaryRequest request, List<MultipartFile> files, Long memberId) throws IOException;
    int processOnce(int batchSize);
}
