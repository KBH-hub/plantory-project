package com.zero.plantory.domain.sharing.dto;

import com.zero.plantory.global.dto.ManageDemand;
import com.zero.plantory.global.dto.ManageLevel;

public class PlantDictionaryResponse {
    private String plantName;

    private ManageLevel manageLevel;      // ENUM
    private String levelLabel;       // 라벨

    private ManageDemand manageDemand;       // ENUM
    private String demandLabel;      // 라벨

    private String fileUrl;
}
