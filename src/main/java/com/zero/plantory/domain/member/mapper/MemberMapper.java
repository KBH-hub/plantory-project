package com.zero.plantory.domain.member.mapper;

import com.zero.plantory.domain.member.dto.request.MyWrittenDeleteRequestVO;
import com.zero.plantory.domain.member.dto.request.MyWrittenListRequestVO;
import com.zero.plantory.domain.member.vo.MyWrittenListVO;
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
    int deleteMyWrittenAll(MyWrittenDeleteRequestVO request);
    int deleteMyWrittenSharing(MyWrittenDeleteRequestVO request);
    int deleteMyWrittenQuestion(MyWrittenDeleteRequestVO request);
}
