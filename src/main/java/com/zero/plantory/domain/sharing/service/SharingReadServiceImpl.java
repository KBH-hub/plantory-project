package com.zero.plantory.domain.sharing.service;

import com.zero.plantory.domain.sharing.mapper.SharingMapper;
import com.zero.plantory.domain.sharing.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SharingReadServiceImpl implements SharingReadService {

    private final SharingMapper sharingMapper;

    @Override
    public List<SharingCardListVO> getSharingList(SharingSearchVO vo) {
        return sharingMapper.selectSharingListByAddressAndKeyword(vo);
    }

    @Override
    public List<SharingPopularVO> getPopularSharingList() {
        return sharingMapper.selectPopularSharingList();
    }

    @Override
    public SelectSharingDetailVO getSharingDetail(Long sharingId) {
        return sharingMapper.selectSharingDetail(sharingId);
    }

    @Override
    public List<SelectCommentListVO> getSharingComments(Long sharingId) {
        return sharingMapper.selectSharingComments(sharingId);
    }

    @Override
    public List<SharingPartnerVO> getMessagePartners(Long receiverId, Long sharingId) {
        return sharingMapper.selectSharingMessagePartners(receiverId, sharingId);
    }

    @Override
    public List<SharingHistoryVO> getMySharingGiven(Long memberId) {
        return sharingMapper.selectMySharingGiven(memberId);
    }

    @Override
    public List<SharingHistoryVO> getMySharingReceived(Long memberId) {
        return sharingMapper.selectMySharingReceived(memberId);
    }



}
