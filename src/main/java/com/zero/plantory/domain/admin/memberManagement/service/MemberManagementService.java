package com.zero.plantory.domain.admin.memberManagement.service;

import com.zero.plantory.domain.admin.memberManagement.dto.MemberManagementPageResponse;
import com.zero.plantory.global.dto.DeleteTargetType;


public interface MemberManagementService {
    MemberManagementPageResponse getMemberList(String keyword, int limit, int offset);
    int deleteContent(DeleteTargetType targetType, Long targetId);
}
