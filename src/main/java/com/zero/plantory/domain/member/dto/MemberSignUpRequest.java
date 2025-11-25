package com.zero.plantory.domain.member.dto;

import com.zero.plantory.global.vo.MemberVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberSignUpRequest {
    private String membername;
    private String password;
    private String nickname;
    private String phone;
    private String address;
}
