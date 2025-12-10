package com.zero.plantory.domain.notice.service;

import com.zero.plantory.global.dto.NoticeDTO;
import com.zero.plantory.global.dto.NoticeTargetType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class NoticeServiceTest {

    @Autowired
    private NoticeService noticeService;

    @Test
    void getNoticeByReceiverTest() {
        Long receiverId = 1L;

        List<NoticeDTO> result =noticeService.getNoticeByReceiver(receiverId);

        log.info("result={}", result);
    }

    @Test
    void registerNoticeTest() {
        NoticeDTO noticeDTO = NoticeDTO.builder()
                .receiverId(2L)
                .targetType(NoticeTargetType.SHARING)
                .targetId(5L)
                .content("test알림입니다.")
                .build();

        int result = noticeService.registerNotice(noticeDTO);

        log.info(String.valueOf(result));
    }

    @Test
    void updateNoticeReadFlag() {
        Long noticeId = 1L;

        int result = noticeService.updateNoticeReadFlag(noticeId);

        log.info(String.valueOf(result));
    }

    @Test
    void removeAllNoticeTest() {
        Long receiverId = 1L;

        int result = noticeService.removeAllNotice(receiverId);

        log.info(String.valueOf(result));
    }
}