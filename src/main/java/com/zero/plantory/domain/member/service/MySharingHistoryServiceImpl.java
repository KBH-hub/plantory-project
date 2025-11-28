package com.zero.plantory.domain.member.service;

import com.zero.plantory.domain.member.dto.MySharingHistoryListRequest;
import com.zero.plantory.domain.member.dto.MySharingHistoryListResponse;
import com.zero.plantory.domain.member.mapper.MySharingHistoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MySharingHistoryServiceImpl implements MySharingHistoryService {

    private final MySharingHistoryMapper mySharingHistoryMapper;

    @Override
    public int getInterestCount(Long memberId) {
        return mySharingHistoryMapper.countByInterestCount(memberId);
    }

    @Override
    public int getCompletedSharingCount(Long memberId) {
        return mySharingHistoryMapper.countByCompletedSharingCount(memberId);
    }

    @Override
    public List<MySharingHistoryListResponse> getMySharingHistoryList(MySharingHistoryListRequest request) {
        return mySharingHistoryMapper.selectMySharingList(request);
    }
}
