package com.zero.plantory.domain.profile.service;

import com.zero.plantory.domain.profile.dto.ProfileWrittenDeleteRequest;
import com.zero.plantory.domain.profile.dto.ProfileWrittenListRequest;
import com.zero.plantory.domain.profile.dto.ProfileWrittenListResponse;
import com.zero.plantory.domain.profile.mapper.ProfileContentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileContentServiceImpl implements ProfileContentService {

    private final ProfileContentMapper profileContentMapper;

    @Override
    public List<ProfileWrittenListResponse> getProfileWrittenListAll(ProfileWrittenListRequest request) {
        return profileContentMapper.selectProfileWrittenListAll(request);
    }

    @Override
    public List<ProfileWrittenListResponse> getProfileWrittenListSharing(ProfileWrittenListRequest request) {
        return profileContentMapper.selectProfileWrittenListSharing(request);
    }

    @Override
    public List<ProfileWrittenListResponse> getProfileWrittenListQuestion(ProfileWrittenListRequest request) {
        return profileContentMapper.selectProfileWrittenListQuestion(request);
    }

    @Override
    @Transactional
    public boolean deleteProfileWrittenSharing(ProfileWrittenDeleteRequest request) {
        return profileContentMapper.deleteProfileWrittenSharing(request) > 0;
    }

    @Override
    @Transactional
    public boolean deleteProfileWrittenQuestion(ProfileWrittenDeleteRequest request) {
        return profileContentMapper.deleteProfileWrittenQuestion(request) > 0;
    }

    @Override
    public List<ProfileWrittenListResponse> searchProfileCommentAll(ProfileWrittenListRequest request) {
        return profileContentMapper.selectProfileCommentSearchAll(request);
    }

    @Override
    public List<ProfileWrittenListResponse> searchProfileCommentSharing(ProfileWrittenListRequest request) {
        return profileContentMapper.selectProfileCommentSearchSharing(request);
    }

    @Override
    public List<ProfileWrittenListResponse> searchProfileCommentQuestion(ProfileWrittenListRequest request) {
        return profileContentMapper.selectProfileCommentSearchQuestion(request);
    }
}
