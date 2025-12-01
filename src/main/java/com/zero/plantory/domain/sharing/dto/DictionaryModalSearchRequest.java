package com.zero.plantory.domain.sharing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DictionaryModalSearchRequest {
    private String id;         // cntntsNo
    private String plantName;  // cntntsSj
    private String fileUrl;    // 썸네일
    private String type;       // "garden" or "dry"
}
