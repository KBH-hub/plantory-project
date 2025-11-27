package com.zero.plantory.domain.message.service;

import com.zero.plantory.domain.message.dto.MessageListResponse;
import com.zero.plantory.domain.message.dto.MessageRequest;
import com.zero.plantory.domain.message.dto.MessageResponse;
import com.zero.plantory.domain.message.dto.MessageSearchRequest;
import com.zero.plantory.domain.message.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    MessageMapper messageMapper;

    @Override
    public List<MessageListResponse> getMessageList(MessageSearchRequest request) {
        List<MessageListResponse> messageList = messageMapper.selectMessages(request);
        return messageList;
    }

    @Override
    public int removeMessages(List<Long> messageIds) {
        return messageMapper.deleteMessages(messageIds);
    }

    @Override
    public int removeSenderMessages(List<Long> messageIds) {
        return messageMapper.deleteSenderMessages(messageIds);
    }

    @Override
    public MessageResponse findMessageWriteInfo(Long senderId, String targetType, Long targetId) {
        MessageResponse message = messageMapper.selectMessageWriteInfo(senderId, targetType, targetId);

        return message;
    }


    @Override
    public int registerMessage(MessageRequest request) {
        if (request.getTitle() == null || "".equals(request.getTitle()))
            throw new IllegalArgumentException("쪽지 전송 필수값(제목) 누락");

        if (request.getContent() == null || "".equals(request.getContent()))
            throw new IllegalArgumentException("메시지 전송 필수값(내용) 누락");

        return messageMapper.insertMessage(request);
    }

    @Override
    public MessageResponse findMessageDetail(Long messageId, Long viewerId) {
        MessageResponse message = messageMapper.selectMessageDetail(messageId);
        if (message == null) {
            throw new IllegalStateException("메시지 존재하지 않음");
        }

        if (message.getReceiverId().equals(viewerId)) {
            if (message.getReadFlag() == null) {
                messageMapper.updateReadFlag(messageId);
            }
        }

        return message;
    }


}
