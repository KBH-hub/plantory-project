package com.zero.plantory.domain.member.service;

import com.zero.plantory.domain.member.vo.MemberVO;

public interface MemberService {
    boolean isDuplicateMembername(String membername);
    boolean isDuplicateNickname(String nickname);
    boolean signUpMember(MemberVO memberVO);
    MemberVO login(String membername, String password);
}
