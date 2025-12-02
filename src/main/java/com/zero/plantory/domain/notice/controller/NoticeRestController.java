package com.zero.plantory.domain.notice.controller;

import com.zero.plantory.domain.notice.service.NoticeService;
import com.zero.plantory.global.dto.NoticeDTO;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NoticeRestController {

    @Autowired
    NoticeService noticeService;

    @GetMapping
    public List<NoticeDTO> getNoticeByReceiver(@RequestParam("receiverId") Long receiverId) {
        return noticeService.getNoticeByReceiver(receiverId);
    }

    @PostMapping
    public int registerNotice(@RequestBody NoticeDTO noticeDTO) {
        return noticeService.registerNotice(noticeDTO);
    }

    @PutMapping
    public int updateNotice(@RequestParam("noticeId") Long noticeId) {
        return noticeService.updateNoticeReadFlag(noticeId);
    }

    @DeleteMapping
    public int removeAllNotice(@RequestParam("receiverId") Long receiverId) {
        return noticeService.removeAllNotice(receiverId);
    }

}
