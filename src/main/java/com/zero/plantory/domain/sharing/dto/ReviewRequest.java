package com.zero.plantory.domain.sharing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {
    private int manner;         // 1~3
    private int reShare;        // 0 or 1
    private Integer satisfaction; // 분양자 null 가능
}

