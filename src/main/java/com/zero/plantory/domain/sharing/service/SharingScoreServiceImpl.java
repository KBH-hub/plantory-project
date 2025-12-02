package com.zero.plantory.domain.sharing.service;

import com.zero.plantory.domain.notice.NoticeMapper;
import com.zero.plantory.domain.sharing.dto.SelectSharingDetailResponse;
import com.zero.plantory.domain.sharing.mapper.SharingMapper;
import com.zero.plantory.global.dto.NoticeTargetType;
import com.zero.plantory.global.dto.NoticeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SharingScoreServiceImpl implements SharingScoreService {
    private final SharingMapper sharingMapper;
    private final NoticeMapper noticeMapper;

    @Override
    public void completeSharing(Long sharingId, Long memberId, Long targetMemberId) {

        SelectSharingDetailResponse sharing = sharingMapper.selectSharingDetail(sharingId);
        if (sharing == null) {
            throw new IllegalArgumentException("존재하지 않는 나눔글입니다.");
        }
        if (!memberId.equals(sharing.getMemberId())) {
            throw new IllegalArgumentException("나눔 완료 권한이 없습니다.");
        }

        if ("true".equals(sharing.getStatus())) {
            throw new IllegalStateException("이미 완료된 나눔입니다.");
        }


        sharingMapper.updateSharingComplete(sharingId, targetMemberId);

        NoticeDTO notice = NoticeDTO.builder()
                .receiverId(targetMemberId)
                .targetId(sharingId)
                .targetType(NoticeTargetType.SHARING)
                .content("분양자가 나눔을 완료했습니다. 후기를 작성해주세요! | 제목: " + sharing.getTitle())
                .build();

        noticeMapper.insertNotice(notice);
    }


    public enum ReviewerType {
        GIVER,      // 분양자
        RECEIVER    // 피분양자
    }

    @Override
    public void registerSharingReview(Long sharingId,
                                      Long loginUserId,
                                      int manner,
                                      int reShare,
                                      Integer satisfaction) {

        SelectSharingDetailResponse sharing = sharingMapper.selectSharingDetail(sharingId);

        ReviewerType reviewerType;

        if (loginUserId.equals(sharing.getMemberId())) {
            reviewerType = ReviewerType.GIVER;
        } else if (loginUserId.equals(sharing.getTargetMemberId())) {
            reviewerType = ReviewerType.RECEIVER;
        } else {
            throw new IllegalArgumentException("후기 작성 권한이 없습니다.");
        }

        // Integer임
        Integer baseRateInt = sharing.getSharingRate();
        double baseRate = (baseRateInt == null) ? 1.0 : baseRateInt.doubleValue();


        double score = calculateRate(baseRate, reviewerType, manner, reShare, satisfaction);

        double finalScore = score + 0.2; // 가산

        // 정규화
        if (finalScore > 100) finalScore = 100;
        if (finalScore < 1) finalScore = 1;

        Long targetMemberId = reviewerType == ReviewerType.GIVER ? sharing.getTargetMemberId() : sharing.getMemberId();

        sharingMapper.updateSharingRate(targetMemberId, finalScore);

        // review flag
        if (reviewerType == ReviewerType.GIVER) {
            sharingMapper.updateReviewFlag(sharingId);
        } else {
            sharingMapper.updateReceiverReviewFlag(sharingId);
        }
    }


    /**
     * 후기 기반 점수를 계산하는 메서드.
     *
     * 분양자(GIVER):
     *   - manner(1~3)
     *   - reShare(0 or 1)
     *
     * 피분양자(RECEIVER):
     *   - manner(1~3)
     *   - reShare(0 or 1)
     *   - satisfaction(1~3)
     */
    private double getMannerWeight(int manner) {
        return switch (manner) {
            case 1 -> 1.05;   // 만족
            case 2 -> 1.00;   // 보통
            case 3 -> 0.95;   // 불만족
            default -> 1.00;
        };
    }

    private double getReShareWeight(int reShare) {
        return reShare == 1 ? 1.03 : 0.97;
    }

    private double getSatisfactionWeight(Integer sat) {
        return switch (sat) {
            case 1 -> 1.05;
            case 2 -> 1.00;
            case 3 -> 0.95;
            default -> 1.00;
        };
    }

    // 최종
    private double calculateRate(double baseRate, ReviewerType reviewerType,
                                 int manner, int reShare, Integer satisfaction) {

        double result = baseRate;

        result *= getMannerWeight(manner);
        result *= getReShareWeight(reShare);

        if (reviewerType == ReviewerType.RECEIVER) {
            result *= getSatisfactionWeight(satisfaction);
        }

        // 정규화
        if (result > 100) result = 100;
        if (result < 1) result = 1;

        return result;
    }


}
