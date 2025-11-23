package com.zero.plantory.domain.sharing.service;

import com.zero.plantory.domain.notice.NoticeMapper;
import com.zero.plantory.domain.sharing.mapper.SharingMapper;
import com.zero.plantory.domain.sharing.vo.SelectSharingDetailVO;
import com.zero.plantory.global.vo.NoticeTargetType;
import com.zero.plantory.global.vo.NoticeVO;
import com.zero.plantory.global.vo.SharingVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SharingScoreServiceImpl implements SharingScoreService {
    private final SharingMapper sharingMapper;
    private final NoticeMapper noticeMapper;

    @Override
    public void completeSharing(Long sharingId, Long ownerId, Long receiverId) {

        sharingMapper.updateSharingComplete(sharingId, receiverId);

        NoticeVO notice = NoticeVO.builder()
                .receiverId(receiverId)
                .targetId(sharingId)
                .targetType(NoticeTargetType.SHARING)
                .content("분양자가 나눔을 완료했습니다. 후기를 작성해주세요!")
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

        // 1) Sharing 데이터 조회
        SelectSharingDetailVO sharing = sharingMapper.selectSharingDetail(sharingId);

        ReviewerType reviewerType;

        // 2) 로그인 사용자와 비교하여 분양자/피분양자 판별
        if (loginUserId.equals(sharing.getMemberId())) {
            reviewerType = ReviewerType.GIVER;
        } else if (loginUserId.equals(sharing.getTargetMemberId())) {
            reviewerType = ReviewerType.RECEIVER;
        } else {
            throw new IllegalArgumentException("후기 작성 권한이 없습니다.");
        }

        // 3) 후기 기반 점수 계산
        int score = calculateReviewScore(reviewerType, manner, reShare, satisfaction);

        // 4) 점수 받을 대상 = 상대방 ID
        Long targetMemberId =
                reviewerType == ReviewerType.GIVER
                        ? sharing.getTargetMemberId()        // 분양자가 후기 → 피분양자가 점수 증가
                        : sharing.getMemberId();             // 피분양자가 후기 → 분양자가 점수 증가

        // 5) 누적 업데이트
        sharingMapper.updateSharingRate(targetMemberId, score);
    }


    private void validateRange(Integer value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }
        if (value < 0 || value > 3) {
            throw new IllegalArgumentException(fieldName + " must be between 0 and 3");
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
    private int calculateReviewScore(ReviewerType reviewerType,
                                     int manner,
                                     int reShare,
                                     Integer satisfaction) {

        // 1) 공통 항목 검증
        validateRange(manner, "manner");

        if (reShare != 0 && reShare != 1) {
            throw new IllegalArgumentException("reShare must be 0 or 1");
        }

        int score = 0;

        // manner + reShare 는 공통
        score += manner;
        score += reShare;


        // 2) 피분양자(RECEIVER)일 때만 satisfaction 추가
        if (reviewerType == ReviewerType.RECEIVER) {
            if (satisfaction == null) {
                throw new IllegalArgumentException("satisfaction is required when reviewer is RECEIVER.");
            }

            validateRange(satisfaction, "satisfaction");
            score += satisfaction;
        }

        return score;
    }


}
