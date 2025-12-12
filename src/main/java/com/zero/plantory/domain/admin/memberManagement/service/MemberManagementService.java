package com.zero.plantory.domain.admin.memberManagement.service;

import com.zero.plantory.domain.admin.memberManagement.dto.MemberManagementResponse;

import java.util.List;


public interface MemberManagementService {
    List<MemberManagementResponse> getMemberList(String keyword, int limit, int offset);
}
