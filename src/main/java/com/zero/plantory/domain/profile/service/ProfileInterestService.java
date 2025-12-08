package com.zero.plantory.domain.profile.service;

import com.zero.plantory.domain.profile.dto.ProfileInterestListRequest;
import com.zero.plantory.domain.profile.dto.ProfileSharingResponse;

import java.util.List;

public interface ProfileInterestService {
    List<ProfileSharingResponse> getProfileInterest(ProfileInterestListRequest request);
}
