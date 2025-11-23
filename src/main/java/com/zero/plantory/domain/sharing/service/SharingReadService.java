package com.zero.plantory.domain.sharing.service;

import com.zero.plantory.domain.sharing.vo.*;

import java.util.List;

public interface SharingReadService {
    /** 나눔 게시글 메인 리스트 조회 */
    List<SharingCardListVO> getSharingList(SharingSearchVO vo);

    /** 인기 나눔글 조회 */
    List<SharingPopularVO> getPopularSharingList();

    /** 나눔글 상세 조회 */
    SelectSharingDetailVO getSharingDetail(Long sharingId);

    /** 상세 댓글 조회 */
    List<SelectCommentListVO> getSharingComments(Long sharingId);

    /** 메시지 상대 목록 조회 */
    List<SharingPartnerVO> getMessagePartners(Long receiverId, Long sharingId);

    /** 내가 나눔한 내역 조회 */
    List<SharingHistoryVO> getMySharingGiven(Long memberId);

    /** 내가 받은 나눔 내역 조회 */
    List<SharingHistoryVO> getMySharingReceived(Long memberId);
}
