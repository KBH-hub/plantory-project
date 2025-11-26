package com.zero.plantory.domain.message.service;

import com.zero.plantory.domain.message.dto.MessageListResponse;
import com.zero.plantory.domain.message.dto.MessageRequest;
import com.zero.plantory.domain.message.dto.MessageResponse;
import com.zero.plantory.domain.message.dto.SearchMessageRequest;
import com.zero.plantory.global.vo.MessageVO;

import java.util.List;

public interface MessageService {
    List<MessageListResponse> getMessageList(SearchMessageRequest request);
    int removeMessages(List<Long> messageIds, Long removerId);
    int removeSenderMessages(List<Long> messageIds, Long removerId);
    MessageResponse findMessageWriteInfo(Long senderId, String targetType, Long targetId);
    int registerMessage(MessageRequest request);
    MessageResponse findMessageDetail(Long messageId, Long viewerId);
}
