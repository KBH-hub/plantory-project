package com.zero.plantory.domain.member.service;

import com.zero.plantory.domain.member.mapper.MySharingHistoryMapper;
import com.zero.plantory.domain.member.vo.MySharingHistoryListRequestVO;
import com.zero.plantory.domain.member.vo.MySharingHistoryVO;
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
    public List<MySharingHistoryVO> getMySharingHistoryList(MySharingHistoryListRequestVO request) {
        return mySharingHistoryMapper.selectMySharingList(request);
    }
}
