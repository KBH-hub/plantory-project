package com.zero.plantory.domain.sharing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SharingSearchRequest {
    private String userAddress;
    private String keyword;
    private int limit;
    private int offset;
}

