package com.zero.plantory.domain.member.mapper;

import com.zero.plantory.domain.profile.dto.MemberResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {
    int countByMembername(@Param("membername") String membername);
    int countByNickname(@Param("nickname") String nickname);
    int insertMember(MemberResponse memberResponse);
    MemberResponse selectByMembername(@Param("membername") String membername);
    MemberResponse selectByMemberId(@Param("memberId") Long memberId);
    void updatePassword(@Param("password") String password, @Param("memberId") Long memberId);
}

