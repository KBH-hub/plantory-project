package com.zero.plantory.domain.member.service;


import com.zero.plantory.domain.member.dto.MemberSignUpRequest;
import com.zero.plantory.global.vo.MemberVO;

public interface MemberService {
    boolean isDuplicateMembername(String membername);
    boolean isDuplicateNickname(String nickname);
    void signUp(MemberSignUpRequest request);
    MemberVO login(String membername, String password);

}
