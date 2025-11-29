package com.zero.plantory.domain.profile.service;

import com.zero.plantory.domain.profile.dto.ProfileSharingHistoryListRequest;
import com.zero.plantory.domain.profile.dto.ProfileSharingHistoryListResponse;
import com.zero.plantory.domain.profile.mapper.ProfileSharingHistoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileSharingHistoryServiceImpl implements ProfileSharingHistoryService {

    private final ProfileSharingHistoryMapper profileSharingHistoryMapper;

    @Override
    public int getInterestCount(Long memberId) {
        return profileSharingHistoryMapper.countByInterestCount(memberId);
    }

    @Override
    public int getCompletedSharingCount(Long memberId) {
        return profileSharingHistoryMapper.countByCompletedSharingCount(memberId);
    }

    @Override
    public List<ProfileSharingHistoryListResponse> getProfileSharingHistoryList(ProfileSharingHistoryListRequest request) {
        return profileSharingHistoryMapper.selectProfileSharingList(request);
    }
}
