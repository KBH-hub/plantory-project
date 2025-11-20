package com.zero.plantory.domain.message.mapper;

import com.zero.plantory.domain.message.vo.MessageVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {
    List<MessageVO> selectMessages(Long memberId, String boxType, String targetType, String title);
    int updateReadFlag(Long messageId);
    int deleteMessages(List<Long> messageIds);
    MessageVO selectMessageWriteInfo(Long senderId, String targetType, Long targetId);
    int insertMessage(MessageVO message);
    MessageVO selectMessageDetail(Long messageId, Long senderId, String targetType, Long targetId);
}
