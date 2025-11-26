package com.zero.plantory.domain.message.dto;

import com.zero.plantory.global.vo.MessageTargetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageResponse {
    private Long messageId;
    private Long senderId;
    private Long receiverId;
    private String title;
    private String content;
    private MessageTargetType targetType;
    private Long targetId;
    private Date createdAt;
    private Date readFlag;
    private Date delFlag;
}
