package com.zero.plantory.domain.member.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberUpdateRequestVO {
    private Long memberId;
    private String nickname;
    private String phone;
    private String address;
}

