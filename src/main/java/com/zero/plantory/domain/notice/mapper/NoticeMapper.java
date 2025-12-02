package com.zero.plantory.domain.notice.mapper;

import com.zero.plantory.global.dto.NoticeDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NoticeMapper {
    List<NoticeDTO> selectNoticesByReceiver(@Param("receiverId") Long receiverId);
    int insertNotice(NoticeDTO noticeDTO);
    int markNoticeAsRead(@Param("noticeId") Long noticeId, @Param("receiverId") Long receiverId);
    int deleteNotice(@Param("noticeId") Long noticeId, @Param("receiverId") Long receiverId);
}
