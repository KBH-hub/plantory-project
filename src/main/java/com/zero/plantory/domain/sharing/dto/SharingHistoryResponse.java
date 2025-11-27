package com.zero.plantory.domain.sharing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SharingHistoryResponse {
    private Long sharingId;
    private Long partnerId;   // 나눔한 경우: target_member_id / 나눔받은 경우: member_id
    private String partnerNickname;
    private String title;
    private LocalDateTime createdAt;
}
