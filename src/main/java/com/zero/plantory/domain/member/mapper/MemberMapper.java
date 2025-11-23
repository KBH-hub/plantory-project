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
}

