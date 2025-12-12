package com.zero.plantory.domain.admin.memberManagement.service;

import com.zero.plantory.domain.admin.memberManagement.dto.MemberManagementResponse;
import com.zero.plantory.domain.admin.memberManagement.mapper.MemberManagementMapper;
import com.zero.plantory.domain.profile.dto.MemberResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberManagementServiceImpl implements MemberManagementService{

    private final MemberManagementMapper memberManagementMapper;

    @Override
    public List<MemberManagementResponse> getMemberList(String keyword, int limit, int offset) {

        return memberManagementMapper.selectMemberList(keyword, limit, offset);
    }

}
