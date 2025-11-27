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
public class MyPlantResponse {
    private Long myplantId;
    private Long memberId;
    private String name;
    private String type;
    private Date startAt;
    private Date endDate;
    private int interval;
    private String soil;
    private String temperature;
    private String imageUrl;
    private Date createdAt;
    private Date delFlag;
}
