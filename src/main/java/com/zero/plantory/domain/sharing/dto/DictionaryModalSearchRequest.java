package com.zero.plantory.domain.sharing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DictionaryModalSearchRequest {
    private String id;
    private String plantName;
    private String fileUrl;
    private String type;
}
