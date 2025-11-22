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
public class NoticeVO {
    private Long noticeId;
    private Long receiverId;
    private NoticeTargetType targetType;
    private Long targetId;
    private String content;
    private Date readFlag;
    private Date createdAt;
    private Date delFlag;

}
