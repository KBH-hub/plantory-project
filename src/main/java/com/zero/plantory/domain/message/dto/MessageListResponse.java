package com.zero.plantory.domain.message.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageListResponse {
    private Long messageId;
    private Long senderId;
    private String senderNickname;
    private Long receiverId;
    private String receiverNickname;
    private String title;
    private String content;
    private String targetType;
    private String targetTitle;
    private Date createdAt;
    private Date readFlag;
    private Date delFlag;
    private Date senderDelFlag;
    private Long totalCount;
}
