package com.zero.plantory.domain.sharing.service;

import com.zero.plantory.domain.image.mapper.ImageMapper;
import com.zero.plantory.domain.sharing.dto.*;
import com.zero.plantory.domain.sharing.mapper.SharingMapper;
import com.zero.plantory.global.dto.ImageTargetType;
import com.zero.plantory.global.dto.ImageDTO;
import com.zero.plantory.global.dto.ManageLevel;
import com.zero.plantory.global.dto.ManageDemand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SharingReadServiceImpl implements SharingReadService {

    private final SharingMapper sharingMapper;
    private final ImageMapper imageMapper;

    @Override
    public List<SharingCardListResponse> getSharingList(SharingSearchRequest request) {
        return sharingMapper.selectSharingListByAddressAndKeyword(request);
    }

    @Override
    public int countInterest(Long memberId) {
        return sharingMapper.countInterestByMemberId(memberId);
    }

    @Override
    public List<SharingPopularResponse> getPopularSharingList(SharingSearchRequest request) {
        return sharingMapper.selectPopularSharingList(request);
    }

    @Override
    public SelectSharingDetailResponse getSharingDetail(Long sharingId) {
        SelectSharingDetailResponse detail = sharingMapper.selectSharingDetail(sharingId);

        if (detail == null) {
            return null;
        }
        if (detail.getManagementLevel() != null) {
            detail.setManagementLevelLabel(
                    ManageLevel.valueOf(detail.getManagementLevel()).getLabel()
            );
        }

        if (detail.getManagementNeeds() != null) {
            detail.setManagementNeedsLabel(
                    ManageDemand.valueOf(detail.getManagementNeeds()).getLabel()
            );
        }

        List<ImageDTO> images = imageMapper.selectImagesByTarget(ImageTargetType.SHARING, sharingId);
        detail.setImages(images);

        return detail;
    }

    @Override
    public List<SelectCommentListResponse> getSharingComments(Long sharingId) {
        return sharingMapper.selectSharingComments(sharingId);
    }

    @Override
    public List<SharingPartnerResponse> getMessagePartners(Long receiverId, Long sharingId) {
        return sharingMapper.selectSharingMessagePartners(receiverId, sharingId);
    }

    @Override
    public List<SharingHistoryResponse> getMySharingGiven(Long memberId) {
        return sharingMapper.selectProfileSharingGiven(memberId);
    }

    @Override
    public List<SharingHistoryResponse> getMySharingReceived(Long memberId) {
        return sharingMapper.selectProfileSharingReceived(memberId);
    }



    @Override
    public ReviewInfoResponse getReviewInfo(Long sharingId, Long memberId) {

        SelectSharingDetailResponse sharing = sharingMapper.selectSharingDetail(sharingId);

        SharingScoreServiceImpl.ReviewerType type;

        if (memberId.equals(sharing.getMemberId())) {
            type = SharingScoreServiceImpl.ReviewerType.GIVER;
            ReviewInfoResponse query =
                    sharingMapper.selectReviewInfoForGiver(sharingId, memberId);
            if (query == null) {
                throw new IllegalStateException("후기 정보를 조회할 수 없습니다.");
            }
            return ReviewInfoResponse.builder()
                    .reviewerType(type)
                    .sharingId(query.getSharingId())
                    .partnerId(query.getPartnerId())
                    .partnerNickname(query.getPartnerNickname())
                    .title(query.getTitle())
                    .createdAt(query.getCreatedAt())
                    .build();

        } else if (memberId.equals(sharing.getTargetMemberId())) {
            type = SharingScoreServiceImpl.ReviewerType.RECEIVER;
            ReviewInfoResponse query =
                    sharingMapper.selectReviewInfoForReceiver(sharingId, memberId);
            if (query == null) {
                throw new IllegalStateException("후기 정보를 조회할 수 없습니다.");
            }

            return ReviewInfoResponse.builder()
                    .reviewerType(type)
                    .sharingId(query.getSharingId())
                    .partnerId(query.getPartnerId())
                    .partnerNickname(query.getPartnerNickname())
                    .title(query.getTitle())
                    .createdAt(query.getCreatedAt())
                    .build();

        } else {
            throw new IllegalArgumentException("후기 작성 권한이 없습니다.");
        }
    }



}
