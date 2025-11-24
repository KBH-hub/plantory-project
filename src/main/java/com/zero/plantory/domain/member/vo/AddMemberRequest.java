package com.zero.plantory.domain.member.vo;

import lombok.Data;

@Data
public class AddMemberRequest {
    private String nickname;
    private String password;
}
