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
    private Long partnerId;
    private String partnerNickname;
    private String title;
    private LocalDateTime createdAt;
}
