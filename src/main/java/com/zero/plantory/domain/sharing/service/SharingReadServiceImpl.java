package com.zero.plantory.domain.sharing.service;

import com.zero.plantory.domain.image.ImageMapper;
import com.zero.plantory.domain.sharing.dto.*;
import com.zero.plantory.domain.sharing.mapper.SharingMapper;
import com.zero.plantory.global.dto.ImageTargetType;
import com.zero.plantory.global.dto.ImageDTO;
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
    public List<SharingPopularResponse> getPopularSharingList() {
        return sharingMapper.selectPopularSharingList();
    }

    @Override
    public SelectSharingDetailResponse getSharingDetail(Long sharingId) {
        SelectSharingDetailResponse detail = sharingMapper.selectSharingDetail(sharingId);

        if (detail == null) {
            return null;
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



//    @Override
//    public ReviewInfoResponse getReviewInfoForGiver(Long sharingId, Long loginMemberId) {
//
//        ReviewInfoResponse result =
//                sharingMapper.selectReviewInfoForGiver(sharingId, loginMemberId);
//
//        if (result == null) {
//            throw new IllegalArgumentException("후기 작성 권한이 없습니다(분양자).");
//        }
//
//        return ReviewInfoResponse.builder()
//                .sharingId(result.getSharingId())
//                .partnerId(result.getPartnerId())
//                .partnerNickname(result.getPartnerNickname())
//                .title(result.getTitle())
//                .createdAt(result.getCreatedAt())
//                .reviewerType(SharingScoreServiceImpl.ReviewerType.GIVER).build();
//    }
//
//    @Override
//    public ReviewInfoResponse getReviewInfoForReceiver(Long sharingId, Long loginMemberId) {
//
//        ReviewInfoResponse result =
//                sharingMapper.selectReviewInfoForReceiver(sharingId, loginMemberId);
//
//        if (result == null) {
//            throw new IllegalArgumentException("후기 작성 권한이 없습니다(피분양자).");
//        }
//
//        return ReviewInfoResponse.builder()
//                .sharingId(result.getSharingId())
//                .partnerId(result.getPartnerId())
//                .partnerNickname(result.getPartnerNickname())
//                .title(result.getTitle())
//                .createdAt(result.getCreatedAt())
//                .reviewerType(SharingScoreServiceImpl.ReviewerType.RECEIVER).build();
//    }
    @Override
    public ReviewInfoResponse getReviewInfo(Long sharingId, Long memberId) {

        SelectSharingDetailResponse sharing = sharingMapper.selectSharingDetail(sharingId);

        SharingScoreServiceImpl.ReviewerType type;

        if (memberId.equals(sharing.getMemberId())) {
            type = SharingScoreServiceImpl.ReviewerType.GIVER;
            ReviewInfoResponse query =
                    sharingMapper.selectReviewInfoForGiver(sharingId, memberId);

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
