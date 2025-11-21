package com.zero.plantory.domain.sharing.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SharingCardListVO {
    private Long sharingId;
    private String title;
    private Integer interestNum;
    private String status;
    private LocalDateTime createdAt;

    private Integer commentCount;
    private String imageUrl;
}
