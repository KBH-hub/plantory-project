package com.zero.plantory.domain.message.mapper;

import com.zero.plantory.domain.message.dto.MessageListResponse;
import com.zero.plantory.domain.message.dto.MessageRequest;
import com.zero.plantory.domain.message.dto.MessageResponse;
import com.zero.plantory.domain.message.dto.MessageSearchRequest;
import com.zero.plantory.global.dto.MessageTargetType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class MessageMapperTests {
    @Autowired
    MessageMapper messageMapper;

    @Test
    @DisplayName("쪽지 리스트 화면 - 받은 쪽지함")
    void selectMessagesTest() {
        MessageSearchRequest dto = new MessageSearchRequest().builder()
                .memberId(21L) // 받은 사람 아이디
                .boxType("RECEIVED") // 받은 쪽지함
                .targetType(null) // 나눔 or 질문 or 모두
                .title(null) // 검색어
                .limit(10) // 한 화면에 보여줄 개수
                .offset(0) // 조회 시작 번호
                .build();

        List<MessageListResponse> result = messageMapper.selectMessages(dto);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("쪽지 리스트 화면 - 보낸 쪽지함 - 나눔 유형 필터")
    void getMessagesSharingTest() {
        MessageSearchRequest dto = new MessageSearchRequest().builder()
                .memberId(2L)
                .boxType("SENT")
                .targetType("SHARING")
                .title(null)
                .limit(10)
                .offset(0)
                .build();

        List<MessageListResponse> result = messageMapper.selectMessages(dto);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("보낸 쪽지함 - 제목 키워드 검색")
    void selectMessagesSearchTest() {
        MessageSearchRequest dto = new MessageSearchRequest().builder()
                .memberId(2L)
                .boxType("SENT")
                .targetType(null)
                .title("위치")
                .limit(10)
                .offset(0)
                .build();

        List<MessageListResponse> result = messageMapper.selectMessages(dto);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("쪽지 클릭(조회)시 읽음 처리")
    void updateReadFlagTest() {
        Long memberId = 1L;

        int result = messageMapper.updateReadFlag(memberId);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("수신자 쪽지 삭제 처리")
    void deleteMessagesTest() {
        List<Long> messageIds = List.of(1L, 2L);

        int result = messageMapper.deleteMessages(messageIds);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("발신자 쪽지 삭제 처리")
    void deleteSenderMessagesTest() {
        List<Long> messageIds = List.of(1L, 2L);

        int result = messageMapper.deleteSenderMessages(messageIds);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("쪽지 등록 - 쪽지 등록 화면 정보 조회")
    void selectMessageWriteInfoTest() {
        Long senderId = 11L; // 쪽지 보내는 사람
        String targetType = "QUESTION"; // 나눔 글 기준
        Long targetId = 3L; // 나눔 글 id

        MessageResponse result = messageMapper.selectMessageWriteInfo(senderId, targetType, targetId);

        log.info(String.valueOf(result));
    }

    @Test
    @DisplayName("쪽지 등록 - 쪽지 등록 처리")
    void insertMessageTest() {
        MessageRequest dto = new MessageRequest().builder()
                .senderId(3L)
                .receiverId(8L)
                .title("안녕하세요. 테스트 쪽지 제목입니다.")
                .content("테스트 쪽지 내용입니다.")
                .targetType(MessageTargetType.SHARING)
                .targetId(13L)
                .build();

        int result = messageMapper.insertMessage(dto);

        log.info("inserted rows = " + result);
    }

    @Test
    @DisplayName("쪽지 상세 모달 화면")
    void selectMessageDetailTest() {
        Long messageId = 3L;

        MessageResponse result = messageMapper.selectMessageDetail(messageId);

        log.info(String.valueOf(result));
    }

}
