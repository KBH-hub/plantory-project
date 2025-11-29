package com.zero.plantory.domain.member.service;


import com.zero.plantory.domain.profile.dto.MemberResponse;
import com.zero.plantory.domain.profile.dto.MemberSignUpRequest;

public interface MemberService {
    boolean isDuplicateMembername(String membername);
    boolean isDuplicateNickname(String nickname);
    void signUp(MemberSignUpRequest request);
    MemberResponse login(String membername, String password);

}
