package com.zero.plantory.domain.notice.service;

import com.zero.plantory.domain.notice.mapper.NoticeMapper;
import com.zero.plantory.global.dto.NoticeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    NoticeMapper noticeMapper;


    @Override
    public List<NoticeDTO> getNoticeByReceiver(Long receiverId) {
        return noticeMapper.selectNoticesByReceiver(receiverId);
    }

    @Override
    public int registerNotice(NoticeDTO request) {
        return noticeMapper.insertNotice(request);
    }

    @Override
    public int updateNoticeReadFlag(Long noticeId) {
        return noticeMapper.updateNoticeReadFlag(noticeId);
    }

    @Override
    public int removeAllNotice(Long receiverId) {
        return noticeMapper.deleteAllNotice(receiverId);
    }
}
