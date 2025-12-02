package com.zero.plantory.domain.admin.memberManagement.service;

import com.zero.plantory.domain.admin.memberManagement.dto.MemberManagementPageResponse;
import com.zero.plantory.domain.admin.memberManagement.mapper.MemberManagementMapper;
import com.zero.plantory.domain.profile.dto.MemberResponse;
import com.zero.plantory.global.dto.DeleteTargetType;
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
    public MemberManagementPageResponse getMemberList(String keyword, int limit, int offset) {

        int totalCount = memberManagementMapper.selectMemberTotalCount(keyword);
        List<MemberResponse> list = memberManagementMapper.selectMemberList(keyword, limit, offset);

        return MemberManagementPageResponse.builder()
                .totalCount(totalCount)
                .list(list)
                .build();
    }

    @Override
    public int deleteContent(DeleteTargetType targetType, Long targetId) {
        return 0;
    }
}
