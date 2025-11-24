package com.zero.plantory.domain.message.service;

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
    public List<SelectMessageListVO> getMessageList(SelectMessageSearchVO vo) {
        List<SelectMessageListVO> messageList= messageMapper.selectMessages(vo);
        if (messageList.isEmpty()) return List.of();
        return messageList;
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
    public MessageVO findMessageWriteInfo(Long senderId, String targetType, Long targetId) {
        return messageMapper.selectMessageWriteInfo(senderId, targetType, targetId);
    }


    @Override
    public int registerMessage(MessageVO message) {
        if (message.getTitle() == null || "".equals(message.getTitle()))
            throw new IllegalArgumentException("쪽지 전송 필수값(제목) 누락");

        if (message.getContent() == null || "".equals(message.getContent()))
            throw new IllegalArgumentException("메시지 전송 필수값(내용) 누락");
        return messageMapper.insertMessage(message);
    }

    @Override
    public MessageVO findMessageDetail(Long messageId, Long viewerId) {
        MessageVO message = messageMapper.selectMessageDetail(messageId);
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
