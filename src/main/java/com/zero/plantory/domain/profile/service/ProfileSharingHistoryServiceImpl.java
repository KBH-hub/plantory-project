package com.zero.plantory.domain.profile.service;

import com.zero.plantory.domain.profile.dto.ProfileSharingHistoryListRequest;
import com.zero.plantory.domain.profile.dto.ProfileSharingHistoryListResponse;
import com.zero.plantory.domain.profile.dto.ProfileSharingHistoryPageResponse;
import com.zero.plantory.domain.profile.mapper.ProfileSharingHistoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
    public  List<ProfileSharingHistoryListResponse> getMySharingList(ProfileSharingHistoryListRequest request) {
        if (request.getKeyword() != null && request.getKeyword().isBlank()) {
            request.setKeyword(null);
        }
        if (request.getStatus() != null && request.getStatus().isBlank()) {
            request.setStatus(null);
        }

        List<ProfileSharingHistoryListResponse> list;
        list = profileSharingHistoryMapper.selectMySharingList(request);
        return list;
    }

    @Override
    public  List<ProfileSharingHistoryListResponse> getReceivedSharingList(ProfileSharingHistoryListRequest request) {if (request.getKeyword() != null && request.getKeyword().isBlank()) {
        request.setKeyword(null);
    }
        if (request.getStatus() != null && request.getStatus().isBlank()) {
            request.setStatus(null);
        }


        List<ProfileSharingHistoryListResponse> list;
        list = profileSharingHistoryMapper.selectReceivedSharingList(request);
        return list;
    }
//    @Override
//    public ProfileSharingHistoryPageResponse getProfileSharingHistoryList(ProfileSharingHistoryListRequest req) {
//
//        if (req.getStatus() != null && req.getStatus().isBlank()) {
//            req.setStatus(null);
//        }
//
//        List<ProfileSharingHistoryListResponse> list;
//        int totalCount;
//
//        if ("MY".equals(req.getMyType())) {
//            list = profileSharingHistoryMapper.selectProfileSharingList(req);
//            totalCount = profileSharingHistoryMapper.countMySharing(req);
//        } else {
//            list = profileSharingHistoryMapper.selectProfileSharingList(req);
//            totalCount = profileSharingHistoryMapper.countReceivedSharing(req);
//        }
//
//        ProfileSharingHistoryPageResponse response = new ProfileSharingHistoryPageResponse();
//        response.setList(list);
//        response.setTotalCount(totalCount);
//
//        return response;
//    }

}
