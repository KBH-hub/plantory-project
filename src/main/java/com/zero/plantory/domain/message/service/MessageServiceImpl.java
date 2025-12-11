package com.zero.plantory.domain.message.service;

import com.zero.plantory.domain.message.dto.MessageListResponse;
import com.zero.plantory.domain.message.dto.MessageRequest;
import com.zero.plantory.domain.message.dto.MessageResponse;
import com.zero.plantory.domain.message.dto.MessageSearchRequest;
import com.zero.plantory.domain.message.mapper.MessageMapper;
import com.zero.plantory.domain.notice.service.NoticeService;
import com.zero.plantory.domain.sharing.dto.SelectSharingDetailResponse;
import com.zero.plantory.domain.sharing.mapper.SharingMapper;
import com.zero.plantory.domain.sharing.service.SharingScoreService;
import com.zero.plantory.global.dto.NoticeDTO;
import com.zero.plantory.global.dto.NoticeTargetType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageMapper messageMapper;
    private final NoticeService noticeService;
    private final SharingScoreService sharingScoreService;


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
    @Transactional
    public int registerMessage(MessageRequest request) {
        if (request.getTitle() == null || "".equals(request.getTitle()))
            throw new IllegalArgumentException("쪽지 전송 필수값(제목) 누락");

        if (request.getContent() == null || "".equals(request.getContent()))
            throw new IllegalArgumentException("메시지 전송 필수값(내용) 누락");

        final LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        LocalDateTime firstReceivedTime = messageMapper.selectReceiveTime(request);

        if(firstReceivedTime != null) {
            int myFirstSendCount = messageMapper.selectFirstSend(request);

            if (myFirstSendCount == 0) {
                long minutes = Duration.between(firstReceivedTime, now).toMinutes();
                if (minutes < 0) minutes = 0;

                sharingScoreService.applyResponseSpeedScore(
                        request.getTargetId(),
                        request.getSenderId(),
                        minutes
                );
            }
        }

        messageMapper.insertMessage(request);
        NoticeDTO notice = NoticeDTO.builder()
                .receiverId(request.getReceiverId())
                .targetType(NoticeTargetType.MESSAGE)
                .targetId(request.getMessageId())
                .content("새 쪽지 알림 | 제목: "+request.getTitle())
                .build();

        return noticeService.registerNotice(notice);
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
