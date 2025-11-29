package com.zero.plantory.domain.myProfile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberUpdateRequest {
    private Long memberId;
    private String nickname;
    private String phone;
    private String address;
}

