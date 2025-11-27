package com.zero.plantory.domain.member.dto;

import lombok.Data;

@Data
public class AddMemberRequest {
    private String nickname;
    private String password;
}
