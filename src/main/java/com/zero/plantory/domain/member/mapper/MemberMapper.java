package com.zero.plantory.domain.member.mapper;

import com.zero.plantory.domain.member.vo.MemberVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    int countByMembername(String membername);
    int countByNickname(String nickname);
    int insertMember(MemberVO memberVO);
    MemberVO selectByMembername(String membername);

}
