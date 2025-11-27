package com.zero.plantory.domain.plantingCalendar.service;

import com.zero.plantory.domain.image.ImageMapper;
import com.zero.plantory.domain.plantingCalendar.dto.*;
import com.zero.plantory.domain.plantingCalendar.mapper.PlantingCalendarMapper;
import com.zero.plantory.global.config.SolapiConfig;
import com.zero.plantory.global.utils.StorageUploader;
import com.zero.plantory.global.dto.ImageTargetType;
import com.zero.plantory.global.dto.ImageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PlantingCalenderServiceImpl implements PlantingCalenderService {
    private final PlantingCalendarMapper plantingCalendarMapper;
    private final ImageMapper imageMapper;
    private final StorageUploader storageUploader;
    private final SMSService smsService;
    private final SolapiConfig solapi;


    @Override
    public int updatePlantWateringCheck(Long wateringId) {
        return plantingCalendarMapper.updatePlantWateringCheck(wateringId);
    }

    @Override
    @Transactional
    public int removePlantWatering(Long myplantId, Long removerId) {
        List<MyPlantDiaryResponse> list = plantingCalendarMapper.selectMyPlant(removerId);
        for (MyPlantDiaryResponse myPlant : list) {
            if (Objects.equals(myPlant.getMyplantId(), myplantId)) {
                plantingCalendarMapper.updateMyPlantWatering(myplantId);
                plantingCalendarMapper.deletePlantWatering(myplantId);
                return 1;
            }
        }
        throw new IllegalStateException("물주기 삭제 실패(식물 소유자 미일치)");
    }

    @Override
    public List<PlantingCalendarResponse> getWateringCalendar(Long memberId, LocalDateTime startDate, LocalDateTime endDate) {
        return plantingCalendarMapper.selectWateringCalendar(memberId, startDate, endDate);
    }

    @Override
    public List<PlantingCalendarResponse> getDiaryCalendar(Long memberId, LocalDateTime startDate, LocalDateTime endDate) {
        return plantingCalendarMapper.selectDiaryCalendar(memberId, startDate, endDate);
    }

    @Override
    public DiaryResponse findDiaryUpdateInfo(Long diaryId) {
        return plantingCalendarMapper.selectDiaryUpdateInfo(diaryId);
    }

    @Override
    public List<ImageDTO> findDiaryUpdateImageInfo(Long diaryId) {
        return imageMapper.selectImagesByTarget(ImageTargetType.DIARY, diaryId);
    }

    @Override
    @Transactional
    public int updateDiary(DiaryRequest request, List<ImageDTO> delImgList, List<MultipartFile> files, Long memberId) throws IOException {
        int result = 0;
        result += plantingCalendarMapper.updateDiary(request);

        for (ImageDTO image : delImgList) {
            result += imageMapper.softDeleteImagesByTarget(ImageTargetType.DIARY, image.getImageId());
        }

        for (MultipartFile file : files) {
            String url = storageUploader.uploadFile(file);

            ImageDTO image = ImageDTO.builder()
                    .memberId(memberId)
                    .targetType(ImageTargetType.DIARY)
                    .targetId(request.getDiaryId())
                    .fileUrl(url)
                    .fileName(file.getOriginalFilename())
                    .build();
            result += imageMapper.insertImage(image);

        }

        if (result == delImgList.size() + files.size() + 1) {
            return result;
        }
        throw new IllegalStateException("관찰일지 수정 실패(업데이트 누락)");
    }

    @Override
    public List<MyPlantDiaryResponse> getMyPlant(Long memberId) {
        return plantingCalendarMapper.selectMyPlant(memberId);
    }

    @Override
    @Transactional
    public int registerDiary(DiaryRequest request, List<MultipartFile> files, Long memberId) throws IOException {
        int result = 0;
        result += plantingCalendarMapper.insertDiary(request);

        for (MultipartFile file : files) {
            String url = storageUploader.uploadFile(file);

            ImageDTO image = ImageDTO.builder()
                    .memberId(memberId)
                    .targetType(ImageTargetType.DIARY)
                    .targetId(request.getDiaryId())
                    .fileUrl(url)
                    .fileName(file.getOriginalFilename())
                    .build();
            result += imageMapper.insertImage(image);

        }
        if (result == files.size() + 1) {
            return result;
        }
        throw new IllegalStateException("관찰일지 등록 실패(업데이트 누락)");
    }

    @Override
    @Transactional
    public int processOnce(int batchSize) {
        var bases = plantingCalendarMapper.selectMyplantsForWindow(batchSize);

        var tz = ZoneId.of("Asia/Seoul");
        var today0 = LocalDate.now(tz).atStartOfDay();
        var windowStart = today0;
        var windowEnd = today0.plusDays(1);

        int ok = 0;
        for (var b : bases) {
            Integer interval = b.getInterval();
            if (interval == null || interval <= 0) continue;
            if (b.getPhone() == null || b.getPhone().isBlank()) continue;

            var nextAt = computeNextAt(b.getStartAt(), interval, windowStart);

            if (condToday(nextAt, b.getEndDate(), windowStart, windowEnd)) {
                int ins = plantingCalendarMapper.insertWateringAtIgnore(b.getMyplantId(), nextAt);
                if (ins > 0) {
                    String text = "[Plantory] 오늘 \"" + b.getName() + "\" 물주기 알림";
                    try {
                        smsService.sendSMS(SMSRequestDTO.builder()
                                .to(b.getPhone())
                                .from(solapi.from())
                                .text(text)
                                .build());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ok++;
                }
            }
        }
        return ok;
    }

    private static boolean condToday(LocalDateTime nextAt, LocalDateTime endDate, LocalDateTime windowStart, LocalDateTime windowEnd) {
        if (nextAt == null || endDate == null) return false;
        return !nextAt.isBefore(windowStart) && nextAt.isBefore(windowEnd) && !nextAt.isAfter(endDate);
    }

    private static LocalDateTime computeNextAt(LocalDateTime startAt, int intervalDays, LocalDateTime anchor) {
        if (!anchor.isAfter(startAt)) return startAt;
        long secs = Duration.between(startAt, anchor).getSeconds();
        long step = (long) Math.ceil(secs / (double) (intervalDays * 86400L));
        return startAt.plusDays(step * intervalDays);
    }
}
