package com.zero.plantory.domain.member.service;


import com.zero.plantory.global.vo.MemberVO;

public interface MemberService {
    boolean isDuplicateMembername(String membername);
    boolean isDuplicateNickname(String nickname);
    boolean signUp(MemberVO memberVo);
    MemberVO login(String membername, String password);

}
