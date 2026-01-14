package com.zero.plantory.domain.sharing.dto;

import com.zero.plantory.global.dto.ManageDemand;
import com.zero.plantory.global.dto.ManageLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DictionaryModalSearchlResponse {
    private String plantName;

    private ManageLevel manageLevel;
    private String levelLabel;

    private ManageDemand manageDemand;
    private String demandLabel;

    private String fileUrl;
}
