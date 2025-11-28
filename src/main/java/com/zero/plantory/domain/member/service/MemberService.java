package com.zero.plantory.domain.member.service;


import com.zero.plantory.domain.myProfile.dto.MemberResponse;
import com.zero.plantory.domain.myProfile.dto.MemberSignUpRequest;

public interface MemberService {
    boolean isDuplicateMembername(String membername);
    boolean isDuplicateNickname(String nickname);
    void signUp(MemberSignUpRequest request);
    MemberResponse login(String membername, String password);

}
