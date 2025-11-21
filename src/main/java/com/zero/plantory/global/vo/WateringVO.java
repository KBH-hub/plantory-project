package com.zero.plantory.global.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WateringVO {
    private Long wateringId;
    private Long myplantId;
    private Date dateAt;
    private Date checkFlag;
}
