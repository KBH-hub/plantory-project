package com.zero.plantory.domain.sharing.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SharingSearchVO {
    private String userAddress;
    private String keyword;
    private int limit;
    private int offset;
}

