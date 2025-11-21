package com.zero.plantory.domain.myPlant.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPlantSearchVO {
    private Long myplantId;
    private String name;
    private Date startAt;
}
