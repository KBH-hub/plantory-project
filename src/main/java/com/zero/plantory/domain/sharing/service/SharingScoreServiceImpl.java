package com.zero.plantory.domain.sharing.service;

import com.zero.plantory.domain.notice.mapper.NoticeMapper;
import com.zero.plantory.domain.sharing.dto.SelectSharingDetailResponse;
import com.zero.plantory.domain.sharing.mapper.SharingMapper;
import com.zero.plantory.global.dto.NoticeTargetType;
import com.zero.plantory.global.dto.NoticeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class SharingScoreServiceImpl implements SharingScoreService {
    private final SharingMapper sharingMapper;
    private final NoticeMapper noticeMapper;

    private static final double RESPONSE_K = 150.0;
    private static final double RESPONSE_WEIGHT = 0.03;

    private static final int MAX_COMPLETE = 10;
    private static final double COMPLETE_WEIGHT = 0.015;

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

        int completeCount = sharingMapper.countCompletedSharingByMemberId(memberId);

        applyCompleteScore(sharingId, memberId, completeCount);

        NoticeDTO notice = NoticeDTO.builder()
                .receiverId(targetMemberId)
                .targetId(sharingId)
                .targetType(NoticeTargetType.SHARING_REVIEW)
                .content("나눔 완료 알림(후기 작성하기) | 제목: " + sharing.getTitle())
                .build();

        noticeMapper.insertNotice(notice);
    }


    public enum ReviewerType {
        GIVER,
        RECEIVER
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

        BigDecimal baseRate = sharing.getSharingRate();
        if (baseRate == null) baseRate = new BigDecimal("1.00");


        BigDecimal score = calculateRate(baseRate, reviewerType, manner, reShare, satisfaction);

        BigDecimal finalScore = score;

        finalScore = clamp14(finalScore);

        Long targetMemberId = (reviewerType == ReviewerType.GIVER)
                ? sharing.getTargetMemberId()
                : sharing.getMemberId();

        sharingMapper.updateSharingRate(targetMemberId, finalScore);

        if (reviewerType == ReviewerType.GIVER) {
            sharingMapper.updateReviewFlag(sharingId);
        } else {
            sharingMapper.updateReceiverReviewFlag(sharingId);
        }
    }

    @Override
    public void applyResponseSpeedScore(Long sharingId,Long memberId, long minutes){
        if(minutes < 0) minutes = 0;

        double responseScore = Math.exp(-(double)minutes / RESPONSE_K);

        double scaledScore = 1.0 + 13.0 * responseScore;

        SelectSharingDetailResponse sharing = sharingMapper.selectSharingDetail(sharingId);
        BigDecimal baseRate = sharing.getSharingRate();
        if(baseRate == null) baseRate = new BigDecimal("7.00");
        double oldRate = baseRate.doubleValue();

        double newRate = oldRate * (1.0 - RESPONSE_WEIGHT) + scaledScore * RESPONSE_WEIGHT;

        sharingMapper.updateSharingRate(memberId, clamp14(BigDecimal.valueOf(newRate)));
    }

    private void applyCompleteScore(Long sharingId,Long memberId, int completeCount) {

        SelectSharingDetailResponse sharing = sharingMapper.selectSharingDetail(sharingId);

        BigDecimal baseRate = sharing.getSharingRate();
        if (baseRate == null) baseRate = new BigDecimal("7.00");
        double oldRate = baseRate.doubleValue();

        double rawScore = Math.log(completeCount + 1) / Math.log(MAX_COMPLETE + 1);
        rawScore = Math.min(rawScore, 1.0);

        double scaledScore = 1.0 + 13.0 * rawScore;

        double newRate = oldRate * (1.0 - COMPLETE_WEIGHT) + scaledScore * COMPLETE_WEIGHT;

        sharingMapper.updateSharingRate(memberId, clamp14(BigDecimal.valueOf(newRate))
        );
    }

    private static final BigDecimal MIN_RATE = new BigDecimal("1.00");
    private static final BigDecimal MAX_RATE = new BigDecimal("14.00");

    private BigDecimal clamp14(BigDecimal value) {
        if (value == null) return MIN_RATE;

        if (value.compareTo(MIN_RATE) < 0) {
            return MIN_RATE;
        }
        if (value.compareTo(MAX_RATE) > 0) {
            return MAX_RATE;
        }
        return value.setScale(2, RoundingMode.HALF_UP);
    }

    private double getMannerWeight(int manner) {
        return switch (manner) {
            case 1 -> 1.05;
            case 2 -> 1.00;
            case 3 -> 0.95;
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

    private BigDecimal calculateRate(BigDecimal baseRate,
                                     ReviewerType reviewerType,
                                     int manner,
                                     int reShare,
                                     Integer satisfaction) {

        BigDecimal result = baseRate;

        result = result.multiply(BigDecimal.valueOf(getMannerWeight(manner)));
        result = result.multiply(BigDecimal.valueOf(getReShareWeight(reShare)));

        if (reviewerType == ReviewerType.RECEIVER) {
            result = result.multiply(BigDecimal.valueOf(getSatisfactionWeight(satisfaction)));
        }

        return clamp14(result);
    }



}
