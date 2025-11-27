package com.zero.plantory.domain.myPlant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPlantSearchNameResponse {
    private Long myplantId;
    private String name;
    private Date startAt;
}
