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
public class MessageVO {
    private Long messageId;
    private Long senderId;
    private Long receiverId;
    private String title;
    private String content;
    private String targetType;
    private Long targetId;
    private Date createdAt;
    private Date readFlag;
    private Date delFlag;
}
