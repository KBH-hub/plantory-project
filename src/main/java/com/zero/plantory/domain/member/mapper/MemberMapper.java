package com.zero.plantory.domain.member.mapper;

import com.zero.plantory.domain.member.vo.*;
import com.zero.plantory.global.vo.ImageVO;
import com.zero.plantory.global.vo.MemberVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemberMapper {
    int countByMembername(@Param("membername") String membername);
    int countByNickname(@Param("nickname") String nickname);
    int insertMember(MemberVO memberVO);
    MemberVO selectByMembername(@Param("membername") String membername);
    MemberVO selectByMemberInfo(@Param("memberId") Long memberId);
    int countByInterestCount(@Param("memberId") Long memberId);
    int countByCompletedSharingCount(@Param("memberId") Long memberId);
    List<MyWrittenListVO> selectMyWrittenListAll(MyWrittenListRequestVO request);
    List<MyWrittenListVO> selectMyWrittenListSharing(MyWrittenListRequestVO request);
    List<MyWrittenListVO> selectMyWrittenListQuestion(MyWrittenListRequestVO request);
    int deleteMyWrittenSharing(MyWrittenDeleteRequestVO request);
    int deleteMyWrittenQuestion(MyWrittenDeleteRequestVO request);
    List<MyWrittenListVO> selectMyCommentSearchAll(MyWrittenListRequestVO request);
    List<MyWrittenListVO> selectMyCommentSearchSharing(MyWrittenListRequestVO request);
    List<MyWrittenListVO> selectMyCommentSearchQuestion(MyWrittenListRequestVO request);
    MemberInfoVO selectMyInfo(Long memberId);
    int updateMyInfo(MemberUpdateRequestVO request);
    int updateNoticeEnabled(@Param("memberId") Long memberId, @Param("enabled") int enabled);
    int deleteMember(Long memberId);
}
