package com.zero.plantory.domain.sharing.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SharingPopularVO {
    private Long sharingId;
    private String title;
    private Integer interestNum;
    private String imageUrl;
}
