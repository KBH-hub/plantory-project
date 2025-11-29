package com.zero.plantory.domain.profile.service;

import com.zero.plantory.domain.profile.dto.ProfileSharingHistoryListRequest;
import com.zero.plantory.domain.profile.dto.ProfileSharingHistoryListResponse;
import com.zero.plantory.domain.profile.mapper.MySharingHistoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileSharingHistoryServiceImpl implements ProfileSharingHistoryService {

    private final MySharingHistoryMapper mySharingHistoryMapper;

    @Override
    public int getInterestCount(Long memberId) {
        return mySharingHistoryMapper.countByInterestCount(memberId);
    }

    @Override
    public int getCompletedSharingCount(Long memberId) {
        return mySharingHistoryMapper.countByCompletedSharingCount(memberId);
    }

    @Override
    public List<ProfileSharingHistoryListResponse> getMySharingHistoryList(ProfileSharingHistoryListRequest request) {
        return mySharingHistoryMapper.selectMySharingList(request);
    }
}
