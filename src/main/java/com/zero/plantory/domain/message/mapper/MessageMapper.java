package com.zero.plantory.domain.message.mapper;

import com.zero.plantory.domain.message.dto.MessageListResponse;
import com.zero.plantory.domain.message.dto.MessageRequest;
import com.zero.plantory.domain.message.dto.MessageResponse;
import com.zero.plantory.domain.message.dto.MessageSearchRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper {
    List<MessageListResponse> selectMessages(MessageSearchRequest vo);
    int updateReadFlag(@Param("messageId") Long messageId);
    int deleteMessages(List<Long> messageIds);
    int deleteSenderMessages(List<Long> messageIds);
    MessageResponse selectMessageWriteInfo(@Param("senderId") Long senderId, @Param("targetType") String targetType, @Param("targetId") Long targetId);
    int insertMessage(MessageRequest message);
    MessageResponse selectMessageDetail(@Param("messageId") Long messageId);
}

