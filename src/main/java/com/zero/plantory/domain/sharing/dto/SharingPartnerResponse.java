package com.zero.plantory.domain.sharing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SharingPartnerResponse {
    private Long memberId;
    private String nickname;
}
