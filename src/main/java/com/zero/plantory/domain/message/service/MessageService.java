package com.zero.plantory.domain.message.service;

import com.zero.plantory.domain.message.dto.MessageListResponse;
import com.zero.plantory.domain.message.dto.MessageRequest;
import com.zero.plantory.domain.message.dto.MessageResponse;
import com.zero.plantory.domain.message.dto.MessageSearchRequest;

import java.util.List;

public interface MessageService {
    List<MessageListResponse> getMessageList(MessageSearchRequest request);
    int removeMessages(List<Long> messageIds, Long removerId);
    int removeSenderMessages(List<Long> messageIds, Long removerId);
    MessageResponse findMessageWriteInfo(Long senderId, String targetType, Long targetId);
    int registerMessage(MessageRequest request);
    MessageResponse findMessageDetail(Long messageId, Long viewerId);
}
