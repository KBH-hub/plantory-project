package com.zero.plantory.domain.sharing.mapper;

import com.zero.plantory.domain.sharing.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface SharingMapper {
    List<SharingCardListResponse> selectSharingListByAddressAndKeyword(SharingSearchRequest request);
    int countInterestByMemberId(Long memberId);
    List<SharingPopularResponse> selectPopularSharingList(SharingSearchRequest request);
    int insertSharing(SharingRequest request);
    SelectSharingDetailResponse selectSharingDetail(Long sharingId);
    List<SelectCommentListResponse> selectSharingComments(Long sharingId);

    /** click interest */
    int countInterest(@Param("memberId") Long memberId, @Param("sharingId") Long sharingId);
    int insertInterest(@Param("memberId") Long memberId, @Param("sharingId") Long sharingId);
    int increaseInterestNum(Long sharingId);

    /** delete interest */
    int deleteInterest(@Param("memberId") Long memberId, @Param("sharingId") Long sharingId);
    int decreaseInterestNum(Long sharingId);

    int insertComment(@Param("sharingId") Long sharingId, @Param("writerId") Long writerId, @Param("content") String content);
    int countProfileComment(@Param("commentId") Long commentId, @Param("sharingId") Long sharingId, @Param("writerId") Long writerId);
    int updateCommentById(CommentRequest request);
    int deleteComment(CommentRequest request);

    /**update sharing*/
    int countProfileSharing(@Param("sharingId") Long sharingId, @Param("memberId") Long memberId);
    int updateSharing(SharingRequest request);

    int deleteSharing(@Param("sharingId") Long sharingId);

    List<SharingPartnerResponse> selectSharingMessagePartners(@Param("receiverId") Long receiverId, @Param("sharingId") Long sharingId);
    int updateSharingComplete(@Param("sharingId") Long sharingId, @Param("targetMemberId") Long targetMemberId);


    List<SharingHistoryResponse> selectProfileSharingGiven(@Param("memberId") Long memberId);
    List<SharingHistoryResponse> selectProfileSharingReceived(@Param("memberId") Long memberId);

    /** Sharing review*/
    int updateSharingRate(@Param("memberId") Long memberId, @Param("score") BigDecimal score);

    ReviewInfoResponse selectReviewInfoForGiver(@Param("sharingId") Long sharingId, @Param("memberId") Long memberId);
    ReviewInfoResponse selectReviewInfoForReceiver(@Param("sharingId") Long sharingId, @Param("memberId") Long memberId);

    void updateReviewFlag(Long sharingId);
    void updateReceiverReviewFlag(Long sharingId);

    int countCompletedSharingByMemberId(Long memberId);



}
