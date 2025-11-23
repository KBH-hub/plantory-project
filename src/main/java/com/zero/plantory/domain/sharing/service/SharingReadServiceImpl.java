package com.zero.plantory.domain.sharing.service;

import com.zero.plantory.domain.image.ImageMapper;
import com.zero.plantory.domain.sharing.mapper.SharingMapper;
import com.zero.plantory.domain.sharing.vo.*;
import com.zero.plantory.global.vo.ImageTargetType;
import com.zero.plantory.global.vo.ImageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SharingReadServiceImpl implements SharingReadService {

    private final SharingMapper sharingMapper;
    private final ImageMapper imageMapper;

    @Override
    public List<SharingCardListVO> getSharingList(SharingSearchVO vo) {
        List<SharingCardListVO> list = sharingMapper.selectSharingListByAddressAndKeyword(vo);

        for (SharingCardListVO item : list) {
            List<ImageVO> images = imageMapper.selectImagesByTarget(
                    ImageTargetType.SHARING,
                    item.getSharingId()
            );

            item.setImageUrl(null);
            if (!images.isEmpty()) {
                item.setImageUrl(images.get(0).getFileUrl());
            }
        }

        return list;
    }

    @Override
    public List<SharingPopularVO> getPopularSharingList() {
        return sharingMapper.selectPopularSharingList();
    }

    @Override
    public SelectSharingDetailVO getSharingDetail(Long sharingId) {
        SelectSharingDetailVO detail = sharingMapper.selectSharingDetail(sharingId);

        if (detail == null) {
            return null;
        }

        List<ImageVO> images = imageMapper.selectImagesByTarget(ImageTargetType.SHARING, sharingId);
        detail.setImages(images);

        return detail;
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
