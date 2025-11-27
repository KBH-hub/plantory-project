package com.zero.plantory.domain.message.service;

import com.zero.plantory.domain.message.dto.MessageListResponse;
import com.zero.plantory.domain.message.dto.MessageRequest;
import com.zero.plantory.domain.message.dto.MessageResponse;
import com.zero.plantory.domain.message.dto.MessageSearchRequest;
import com.zero.plantory.global.dto.BoxType;
import com.zero.plantory.global.dto.MessageTargetType;
import com.zero.plantory.global.dto.TargetType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
class MessageServiceTest {

    @Autowired
    private MessageService messageService;

    @Test
    @DisplayName("쪽지 리스트 조회")
    void getMessageListTest() {
        MessageSearchRequest request = MessageSearchRequest.builder()
                .memberId(20L)
                .boxType(BoxType.RECEIVED)
                .targetType(TargetType.SHARING)
                .title(null)
                .limit(10)
                .offset(0)
                .build();

        List<MessageListResponse> result = messageService.getMessageList(request);

        log.info("result={}", result);

    }

    @Test
    @DisplayName("수신자 메시지 삭제 처리")
    void removeMessageTest() {
        List<Long> messageIds = List.of(1L, 2L);

        int result = messageService.removeMessages(messageIds);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("발신자 메시지 삭제 처리")
    void removeSenderMessageTest() {
        List<Long> messageIds = List.of(3L, 4L);
        Long removerId = 11L;

        int result = messageService.removeSenderMessages(messageIds);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("메시지 전송 시 필요 정보 조회")
    void findMessageWriteInfoTest() {
        Long senderId = 11L; // 쪽지 보내는 사람
        String targetType = "QUESTION"; // 나눔 글 기준
        Long targetId = 3L;

        MessageResponse result = messageService.findMessageWriteInfo(senderId, targetType, targetId);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("메시지 전송 실패 (제목 누락)처리")
    void registerFailByTitleMessageTest() {
        MessageRequest request = MessageRequest.builder()
                .senderId(3L)
                .receiverId(8L)
                .title("")
                .content("테스트 쪽지 내용입니다.")
                .targetType(MessageTargetType.SHARING)
                .targetId(13L)
                .build();

        boolean result;
        try {
            messageService.registerMessage(request);
            result = true;
        } catch (IllegalArgumentException e) {
            result = false;
        }

        Assertions.assertFalse(result);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("메시지 전송 실패 (내용 누락)처리")
    void registerFailByContentMessageTest() {
        MessageRequest request = MessageRequest.builder()
                .senderId(3L)
                .receiverId(8L)
                .title("테스트 제목입니다.")
                .content("")
                .targetType(MessageTargetType.SHARING)
                .targetId(13L)
                .build();

        boolean result;
        try {
            messageService.registerMessage(request);
            result = true;
        } catch (IllegalArgumentException e) {
            result = false;
        }

        Assertions.assertFalse(result);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("발신자 메시지 상세 정보 조회 시 읽음 처리 방지")
    void senderFindMessageDetailTest() {
        Long messageId = 5L;
        Long viewerId = 8L;

        MessageResponse result = messageService.findMessageDetail(messageId, viewerId);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("수신자 메시지 상세 정보 조회 시 읽음 처리")
    void receiverFindMessageDetailTest() {
        Long messageId = 6L;
        Long viewerId = 1L;

        MessageResponse result = messageService.findMessageDetail(messageId, viewerId);

        log.info(String.valueOf(result));
    }

}