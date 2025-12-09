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

        SelectSharingDetailResponse sharingInfo = sharingMapper.selectSharingDetail(sharingId);
        BigDecimal baseRate = sharingInfo.getSharingRate();

        if (baseRate == null) baseRate = new BigDecimal("7.00");

        BigDecimal next = baseRate.add(new BigDecimal("0.20"));
        if (next.compareTo(new BigDecimal("14.00")) > 0) {
            next = new BigDecimal("14.00");
        }

        sharingMapper.updateSharingRate(memberId, next);

        NoticeDTO notice = NoticeDTO.builder()
                .receiverId(targetMemberId)
                .targetId(sharingId)
                .targetType(NoticeTargetType.SHARING_REVIEW)
                .content("나눔 완료 알림(후기 작성하기) | 제목: " + sharing.getTitle())
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

        BigDecimal baseRate = sharing.getSharingRate();
        if (baseRate == null) baseRate = new BigDecimal("1.00");
//        double baseRate = (baseRateInt == null) ? 1.0 : baseRateInt.doubleValue();


        BigDecimal score = calculateRate(baseRate, reviewerType, manner, reShare, satisfaction);

//        BigDecimal finalScore = score.add(new BigDecimal("0.20"));
        BigDecimal finalScore = score;


        // 정규화
        finalScore = clamp14(finalScore);

        Long targetMemberId = (reviewerType == ReviewerType.GIVER)
                ? sharing.getTargetMemberId()
                : sharing.getMemberId();

        // BigDecimal을 그대로 저장
        sharingMapper.updateSharingRate(targetMemberId, finalScore);

        // review flag
        if (reviewerType == ReviewerType.GIVER) {
            sharingMapper.updateReviewFlag(sharingId);
        } else {
            sharingMapper.updateReceiverReviewFlag(sharingId);
        }
    }

    // Normalization
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
    private BigDecimal calculateRate(BigDecimal baseRate,
                                     ReviewerType reviewerType,
                                     int manner,
                                     int reShare,
                                     Integer satisfaction) {

        BigDecimal result = baseRate;

        // BigDecimal × double → multiply(BigDecimal.valueOf())
        result = result.multiply(BigDecimal.valueOf(getMannerWeight(manner)));
        result = result.multiply(BigDecimal.valueOf(getReShareWeight(reShare)));

        if (reviewerType == ReviewerType.RECEIVER) {
            result = result.multiply(BigDecimal.valueOf(getSatisfactionWeight(satisfaction)));
        }

        // 정규화 (1.00 ~ 14.00)
        return clamp14(result);
    }



}
