package com.zero.plantory.domain.sharing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SharingPopularResponse {
    private Long sharingId;
    private String title;
    private Integer interestNum;
    private String imageUrl;
}
