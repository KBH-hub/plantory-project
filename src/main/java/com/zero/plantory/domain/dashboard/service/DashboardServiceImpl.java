package com.zero.plantory.domain.dashboard.service;

import com.zero.plantory.domain.dashboard.dto.RecommendedSharingResponseDTO;
import com.zero.plantory.domain.dashboard.dto.TodayDiaryResponseDTO;
import com.zero.plantory.domain.dashboard.dto.TodayWateringResponseDTO;
import com.zero.plantory.domain.dashboard.mapper.DashboardMapper;
import com.zero.plantory.domain.dashboard.vo.RecommendedSharingVO;
import com.zero.plantory.domain.dashboard.vo.TodayDiaryVO;
import com.zero.plantory.domain.dashboard.vo.TodayWateringVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final DashboardMapper dashboardMapper;

    @Override
    public int countMyPlants(Long memberId) {
        return dashboardMapper.countMyplantsByMemberId(memberId);
    }

    @Override
    public int countTodayWatering(Long memberId) {
        return dashboardMapper.countTodayWatering(memberId);
    }

    @Override
    public int countCareNeededPlants(Long memberId) {
        return dashboardMapper.countCareNeededPlants(memberId);
    }

    @Override
    public List<RecommendedSharingResponseDTO> getRecommendedSharingList() {

        List<RecommendedSharingVO> voList = dashboardMapper.selectRecommendedSharing();
        List<RecommendedSharingResponseDTO> dtoList = new ArrayList<>();

        for (RecommendedSharingVO vo : voList) {

            RecommendedSharingResponseDTO dto = RecommendedSharingResponseDTO.builder()
                    .sharingId(vo.getSharingId())
                    .title(vo.getTitle())
                    .status(vo.getStatus())
                    .createdAt(vo.getCreatedAt())
                    .interestNum(vo.getInterestNum())
                    .commentCount(vo.getCommentCount())
                    .fileUrl(vo.getFileUrl())
                    .build();

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public List<TodayWateringResponseDTO> getTodayWatering(Long memberId) {
        List<TodayWateringVO> voList = dashboardMapper.selectTodayWateringByMemberId(memberId);
        List<TodayWateringResponseDTO> dtoList = new ArrayList<>();

        for (TodayWateringVO vo : voList) {

            TodayWateringResponseDTO dto = TodayWateringResponseDTO.builder()
                    .name(vo.getName())
                    .interval(vo.getInterval())
                    .build();

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public List<TodayDiaryResponseDTO> getTodayDiary(Long memberId) {
        List<TodayDiaryVO> voList = dashboardMapper.selectTodayDiaryByMemberId(memberId);
        List<TodayDiaryResponseDTO> dtoList = new ArrayList<>();

        for (TodayDiaryVO vo : voList) {

            TodayDiaryResponseDTO dto = TodayDiaryResponseDTO.builder()
                    .diaryId(vo.getDiaryId())
                    .myplantId(vo.getMyplantId())
                    .myplantName(vo.getMyplantName())
                    .activity(vo.getActivity())
                    .state(vo.getState())
                    .memo(vo.getMemo())
                    .createdAt(vo.getCreatedAt())
                    .fileUrl(vo.getImageUrl())
                    .build();

            dtoList.add(dto);
        }

        return dtoList;
    }
}
