package com.zero.plantory.domain.message.service;

import com.zero.plantory.domain.message.dto.MessageListResponse;
import com.zero.plantory.domain.message.dto.MessageRequest;
import com.zero.plantory.domain.message.dto.MessageResponse;
import com.zero.plantory.domain.message.dto.SearchMessageRequest;
import com.zero.plantory.domain.message.mapper.MessageMapper;
import com.zero.plantory.domain.message.vo.SelectMessageListVO;
import com.zero.plantory.domain.message.vo.SelectMessageSearchVO;
import com.zero.plantory.global.vo.MessageVO;
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
    public List<MessageListResponse> getMessageList(SearchMessageRequest request) {
        SelectMessageSearchVO vo = SelectMessageSearchVO.builder()
                .memberId(request.getMemberId())
                .boxType(request.getBoxType())
                .targetType(request.getTargetType())
                .title(request.getTitle())
                .offset(request.getOffset())
                .limit(request.getLimit())
                .build();
        List<SelectMessageListVO> messageList = messageMapper.selectMessages(vo);
        List<MessageListResponse> messageListResponse = new ArrayList<>();
        for (SelectMessageListVO selectMessageListVO : messageList) {
            MessageListResponse.builder()
                    .messageId(selectMessageListVO.getMessageId())
                    .senderId(selectMessageListVO.getSenderId())
                    .senderNickname(selectMessageListVO.getSenderNickname())
                    .receiverNickname(selectMessageListVO.getReceiverNickname())
                    .title(selectMessageListVO.getTitle())
                    .targetType(selectMessageListVO.getTargetType())
                    .createdAt(selectMessageListVO.getCreatedAt())
                    .readFlag(selectMessageListVO.getReadFlag())
                    .delFlag(selectMessageListVO.getDelFlag())
                    .build();
        }

        return messageListResponse;
    }

    @Override
    public int removeMessages(List<Long> messageIds, Long removerId) {
        List<Long> deletableIds = new ArrayList<>();
        for (Long messageId : messageIds) {
            MessageVO message = messageMapper.selectMessageDetail(messageId);
            if (message == null) continue;
            if (Objects.equals(removerId, message.getReceiverId())) {
                deletableIds.add(messageId);
            }
        }
        if (deletableIds.isEmpty()) return 0;
        return messageMapper.deleteMessages(deletableIds);
    }

    @Override
    public int removeSenderMessages(List<Long> messageIds, Long removerId) {
        List<Long> deletableIds = new ArrayList<>();
        for (Long messageId : messageIds) {
            MessageVO message = messageMapper.selectMessageDetail(messageId);
            if (message == null) continue;
            if (Objects.equals(removerId, message.getSenderId())) {
                deletableIds.add(messageId);
            }
        }
        if (deletableIds.isEmpty()) return 0;
        return messageMapper.deleteSenderMessages(deletableIds);
    }

    @Override
    public MessageResponse findMessageWriteInfo(Long senderId, String targetType, Long targetId) {
        MessageVO message = messageMapper.selectMessageWriteInfo(senderId, targetType, targetId);
           MessageResponse response = MessageResponse.builder()
                   .messageId(message.getMessageId())
                   .senderId(message.getSenderId())
                   .receiverId(message.getReceiverId())
                   .title(message.getTitle())
                   .content(message.getContent())
                   .targetType(message.getTargetType())
                   .targetId(message.getTargetId())
                   .createdAt(message.getCreatedAt())
                   .readFlag(message.getReadFlag())
                   .delFlag(message.getDelFlag())
                   .build();

        return null;
    }


    @Override
    public int registerMessage(MessageRequest request) {
        if (request.getTitle() == null || "".equals(request.getTitle()))
            throw new IllegalArgumentException("쪽지 전송 필수값(제목) 누락");

        if (request.getContent() == null || "".equals(request.getContent()))
            throw new IllegalArgumentException("메시지 전송 필수값(내용) 누락");

        MessageVO message = MessageVO.builder()
                .messageId(request.getMessageId())
                .senderId(request.getSenderId())
                .receiverId(request.getReceiverId())
                .title(request.getTitle())
                .content(request.getContent())
                .targetType(request.getTargetType())
                .targetId(request.getTargetId())
                .createdAt(request.getCreatedAt())
                .readFlag(request.getReadFlag())
                .delFlag(request.getDelFlag())
                .build();
        return messageMapper.insertMessage(message);
    }

    @Override
    public MessageResponse findMessageDetail(Long messageId, Long viewerId) {
        MessageVO message = messageMapper.selectMessageDetail(messageId);
        MessageResponse response = MessageResponse.builder()
                .messageId(message.getMessageId())
                .senderId(message.getSenderId())
                .receiverId(message.getReceiverId())
                .title(message.getTitle())
                .content(message.getContent())
                .targetType(message.getTargetType())
                .targetId(message.getTargetId())
                .createdAt(message.getCreatedAt())
                .readFlag(message.getReadFlag())
                .delFlag(message.getDelFlag())
                .build();
        if (message == null) {
            throw new IllegalStateException("메시지 존재하지 않음");
        }

        if (message.getReceiverId().equals(viewerId)) {
            if (message.getReadFlag() == null) {
                messageMapper.updateReadFlag(messageId);
            }
        }

        return response;
    }


}
