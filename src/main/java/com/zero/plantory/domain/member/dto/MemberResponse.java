package com.zero.plantory.domain.member.dto;

import com.zero.plantory.global.vo.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberResponse {
    private Long memberId;
    private String membername;
    private String password;
    private String nickname;
    private String phone;
    private String address;
    private Integer sharingRate;
    private Integer skillRate;
    private Integer managementRate;
    private Role role;
    private LocalDateTime stopDay;
    private LocalDateTime delFlag;
    private LocalDateTime createdAt;
    private Integer noticeEnabled;

}
