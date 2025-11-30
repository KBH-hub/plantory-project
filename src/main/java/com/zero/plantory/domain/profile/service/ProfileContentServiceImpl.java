package com.zero.plantory.domain.profile.service;

import com.zero.plantory.domain.profile.dto.ProfileWrittenDeleteRequest;
import com.zero.plantory.domain.profile.dto.ProfileWrittenListRequest;
import com.zero.plantory.domain.profile.dto.ProfileWrittenListResponse;
import com.zero.plantory.domain.profile.mapper.ProfileContentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Override
    public ProfileWrittenPageResult getProfileWrittenList(ProfileWrittenListRequest request, String category) {

        int total = 0;
        List<ProfileWrittenListResponse> list = null;

        switch (category) {
            case "ALL":
                total = profileContentMapper.countProfileWrittenAll(request);
                list = profileContentMapper.selectProfileWrittenListAll(request);
                return new ProfileWrittenPageResult(total, list);

            case "QUESTION":
                total = profileContentMapper.countProfileWrittenQuestion(request);
                list = profileContentMapper.selectProfileWrittenListQuestion(request);
                break;

            case "SHARING":
                total = profileContentMapper.countProfileWrittenSharing(request);
                list = profileContentMapper.selectProfileWrittenListSharing(request);
                break;

            case "COMMENT_ALL": {
                total = profileContentMapper.countProfileCommentAll(request);
                list = profileContentMapper.selectProfileCommentSearchAll(request);
                return new ProfileWrittenPageResult(total, list);
            }

            case "COMMENT":
                total = profileContentMapper.countProfileCommentSharing(request);
                list = profileContentMapper.selectProfileCommentSearchSharing(request);
                break;

            case "ANSWER":
                total = profileContentMapper.countProfileCommentQuestion(request);
                list = profileContentMapper.selectProfileCommentSearchQuestion(request);
                break;

        }

        return new ProfileWrittenPageResult(total, list);
    }

    @Override
    public void deleteWritten(ProfileWrittenDeleteRequest req) {

        if (req.getSharingIds() != null && !req.getSharingIds().isEmpty()) {
            profileContentMapper.deleteProfileWrittenSharing(req);
        }

        if (req.getQuestionIds() != null && !req.getQuestionIds().isEmpty()) {
            profileContentMapper.deleteProfileWrittenQuestion(req);
        }
    }


}
