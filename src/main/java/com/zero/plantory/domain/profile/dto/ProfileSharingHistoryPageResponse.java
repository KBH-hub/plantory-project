package com.zero.plantory.domain.profile.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProfileSharingHistoryPageResponse {
    private int totalCount;
    private List<ProfileSharingHistoryListResponse> list;
}

