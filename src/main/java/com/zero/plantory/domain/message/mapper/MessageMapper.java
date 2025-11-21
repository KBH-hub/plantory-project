package com.zero.plantory.domain.message.mapper;

import com.zero.plantory.domain.message.vo.SelectMessageListVO;
import com.zero.plantory.domain.global.vo.MessageVO;
import com.zero.plantory.domain.message.vo.SelectMessageSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper {
    List<SelectMessageListVO> selectMessages(SelectMessageSearchVO vo);
    int updateReadFlag(@Param("messageId") Long messageId);
    int deleteMessages(List<Long> messageIds);
    MessageVO selectMessageWriteInfo(@Param("senderId") Long senderId, @Param("targetType") String targetType, @Param("targetId") Long targetId);
    int insertMessage(MessageVO message);
    MessageVO selectMessageDetail(@Param("messageId") Long messageId);
}

