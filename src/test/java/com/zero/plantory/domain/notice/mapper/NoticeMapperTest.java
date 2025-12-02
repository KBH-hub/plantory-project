package com.zero.plantory.domain.notice.mapper;

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
    void registerNotice() {
        NoticeDTO vo = NoticeDTO.builder()
                .receiverId(3L)
                .targetType(NoticeTargetType.SHARING)
                .targetId(5L)
                .content("테스트 알림입니다.")
                .build();

        log.info("알림 생성 결과 = {}",  mapper.insertNotice(vo));
    }

    @Test
    @DisplayName("읽음 플래그 업데이트")
    void updateNoticeReadFlag() {
        log.info("읽음 플래그 업데이트  결과 = {}",mapper.updateNoticeReadFlag(1L));
    }

    @Test
    @DisplayName("알림 비우기 (소프트 삭제)")
    void removeAllNotice() {
        log.info("삭제 결과 = {}", mapper.deleteAllNotice(4L));
    }
}
