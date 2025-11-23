package com.zero.plantory.domain.message.service;

import com.zero.plantory.domain.message.vo.SelectMessageListVO;
import com.zero.plantory.domain.message.vo.SelectMessageSearchVO;
import com.zero.plantory.global.vo.MessageVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MessageService {
    List<SelectMessageListVO> getMessageList(SelectMessageSearchVO vo);
    int removeMessages(List<Long> messageIds, Long removerId);
    MessageVO findMessageWriteInfo(Long senderId, String targetType, Long targetId);
    int registerMessage(MessageVO message);
    MessageVO findMessageDetail(Long messageId, Long viewerId);
}
