package com.zero.plantory.domain.message.service;

import com.zero.plantory.domain.message.vo.SelectMessageListVO;
import com.zero.plantory.domain.message.vo.SelectMessageSearchVO;
import com.zero.plantory.global.vo.MessageTargetType;
import com.zero.plantory.global.vo.MessageVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class MessageServiceTest {

    @Autowired
    private MessageService messageService;

    @Test
    @DisplayName("쪽지 리스트 조회")
    void getMessageListTest() {
        SelectMessageSearchVO vo = SelectMessageSearchVO.builder()
                .memberId(2L)
                .boxType("SENT")
                .targetType("SHARING")
                .title(null)
                .limit(10)
                .offset(0)
                .build();

        List<SelectMessageListVO> result = messageService.getMessageList(vo);

        log.info("result={}", result);

    }

    @Test
    @DisplayName("수신자 메시지 삭제 처리")
    void removeMessageTest() {
        List<Long> messageIds = List.of(1L, 2L);
        Long removerId = 1L;

        int result = messageService.removeMessages(messageIds, removerId);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("발신자 메시지 삭제 처리")
    void removeSenderMessageTest() {
        List<Long> messageIds = List.of(3L, 4L);
        Long removerId = 11L;

        int result = messageService.removeSenderMessages(messageIds, removerId);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("메시지 전송 시 필요 정보 조회")
    void findMessageWriteInfoTest() {
        Long senderId = 11L; // 쪽지 보내는 사람
        String targetType = "QUESTION"; // 나눔 글 기준
        Long targetId = 3L;

        MessageVO result = messageService.findMessageWriteInfo(senderId, targetType, targetId);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("메시지 전송 실패 (제목 누락)처리")
    void registerFailByTitleMessageTest() {
        MessageVO vo = new MessageVO().builder()
                .senderId(3L)
                .receiverId(8L)
                .title("")
                .content("테스트 쪽지 내용입니다.")
                .targetType(MessageTargetType.SHARING)
                .targetId(13L)
                .build();

        int result = messageService.registerMessage(vo);

        log.info("inserted rows = " + result);
    }

    @Test
    @DisplayName("메시지 전송 실패 (내용 누락)처리")
    void registerFailByContentMessageTest() {
        MessageVO vo = new MessageVO().builder()
                .senderId(3L)
                .receiverId(8L)
                .title("테스트 제목입니다.")
                .content("")
                .targetType(MessageTargetType.SHARING)
                .targetId(13L)
                .build();

        int result = messageService.registerMessage(vo);

        log.info("inserted rows = " + result);
    }

    @Test
    @DisplayName("발신자 메시지 상세 정보 조회 시 읽음 처리 방지")
    void senderFindMessageDetailTest() {
        Long messageId = 5L;
        Long viewerId = 8L;

        MessageVO result = messageService.findMessageDetail(messageId, viewerId);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("수신자 메시지 상세 정보 조회 시 읽음 처리")
    void receiverFindMessageDetailTest() {
        Long messageId = 6L;
        Long viewerId = 1L;

        MessageVO result = messageService.findMessageDetail(messageId, viewerId);

        log.info(String.valueOf(result));
    }

}