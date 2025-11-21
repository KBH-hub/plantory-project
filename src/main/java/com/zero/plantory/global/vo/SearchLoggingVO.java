package com.zero.plantory.global.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchLoggingVO {
    Long  searchLoggingId;
    String memberId;
    String search_keyword;
    LocalDateTime createdAt;
}
