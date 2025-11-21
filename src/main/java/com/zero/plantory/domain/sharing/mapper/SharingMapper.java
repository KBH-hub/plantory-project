package com.zero.plantory.domain.sharing.mapper;

import com.zero.plantory.domain.sharing.vo.*;
import com.zero.plantory.global.vo.CommentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SharingMapper {
    List<SharingCardListVO> selectSharingListByAddressAndKeyword(SharingSearchVO vo);
    int countInterestByMemberId(Long memberId);
    List<SharingPopularVO> selectPopularSharingList();
    int insertSharing(SharingVO vo);
    SelectSharingDetailVO selectSharingDetail(Long sharingId);
    List<SelectSharingCommentVO> selectSharingComments(Long sharingId);

    /** click interest */
    int countInterest(@Param("memberId") Long memberId, @Param("sharingId") Long sharingId);
    int insertInterest(@Param("memberId") Long memberId, @Param("sharingId") Long sharingId);
    int increaseInterestNum(Long sharingId);

    /** delete interest */
    int deleteInterest(@Param("memberId") Long memberId, @Param("sharingId") Long sharingId);
    int decreaseInterestNum(Long sharingId);

    int insertComment(@Param("sharingId") Long sharingId, @Param("writerId") Long writerId, @Param("content") String content);
    int countMyComment(@Param("commentId") Long commentId, @Param("sharingId") Long sharingId, @Param("writerId") Long writerId);
    int updateCommentById(CommentVO vo);
    int deleteComment(CommentVO vo);

}
