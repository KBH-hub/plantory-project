package com.zero.plantory.domain.notice;

import com.zero.plantory.global.dto.NoticeTargetType;
import com.zero.plantory.global.dto.NoticeDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class NoticeMapperTest {

    @Autowired
    private NoticeMapper mapper;

    @Test
    @DisplayName("안 읽은 알림 조회")
    void selectNoticesByReceiverTest() {
        log.info("조회된 알림 = {}",  mapper.selectNoticesByReceiver(3L));
    }

    @Test
    @DisplayName("알림 생성")
    void insertNoticeTest() {
        NoticeDTO vo = NoticeDTO.builder()
                .receiverId(3L)
                .targetType(NoticeTargetType.SHARING)
                .targetId(5L)
                .content("테스트 알림입니다.")
                .build();

        log.info("알림 생성 결과 = {}",  mapper.insertNotice(vo));
    }

    @Test
    @DisplayName("알림 읽음 처리")
    void markNoticeAsReadTest() {
        log.info("읽음 처리 결과 = {}", mapper.markNoticeAsRead(1L, 3L));
    }

    @Test
    @DisplayName("알림 소프트 삭제")
    void deleteNoticeTest() {
        log.info("삭제 결과 = {}", mapper.deleteNotice(1L, 3L));
    }
}
