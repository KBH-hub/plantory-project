package com.zero.plantory.domain.member.service;

import com.zero.plantory.domain.member.vo.MySharingHistoryListRequestVO;
import com.zero.plantory.domain.member.vo.MySharingHistoryVO;

import java.util.List;

public interface MySharingHistoryService {

    int getInterestCount(Long memberId);

    int getCompletedSharingCount(Long memberId);

    List<MySharingHistoryVO> getMySharingHistoryList(MySharingHistoryListRequestVO request);
}
