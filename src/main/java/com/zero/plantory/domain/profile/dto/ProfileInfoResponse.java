package com.zero.plantory.domain.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileInfoResponse {
    private Long memberId;
    private String membername;
    private String nickname;
    private String phone;
    private String address;
    private String role;
    private Integer noticeEnabled;
    private Integer sharingRate;
    private String delFlag;
}

