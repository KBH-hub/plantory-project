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
public class MyPlantVO {
    private Long myplantId;
    private Long memberId;
    private String name;
    private String type;
    private Date startAt;
    private Date endDate;
    private String interval;
    private String soil;
    private String temperature;
    private Date createdAt;
    private Date delFlag;
}
