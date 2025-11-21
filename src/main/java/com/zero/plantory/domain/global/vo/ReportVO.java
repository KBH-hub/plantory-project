package com.zero.plantory.domain.global.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportVO {
    private Long reportId;
    private Long adminId;
    private Long reporterId;
    private Long targetMemberId;
    private String content;
    private String status;
    private Date createAt;
    private String adminMemo;
    private Date delFlag;
}
