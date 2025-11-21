package com.zero.plantory.domain.member.mapper;

import com.zero.plantory.domain.member.dto.request.MyWrittenListRequestVO;
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
//    List<MyWrittenListRequestVO>  MyWrittenListRequestVO(@Param("memberId") Long memberId);

}
