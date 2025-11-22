package com.zero.plantory.domain.notice;

import com.zero.plantory.global.vo.NoticeTargetType;
import com.zero.plantory.global.vo.NoticeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NoticeMapper {
    List<NoticeVO> selectNoticesByReceiver(@Param("receiverId") Long receiverId);
    int insertNotice(NoticeVO noticeVO);
    int markNoticeAsRead(@Param("noticeId") Long noticeId, @Param("receiverId") Long receiverId);
    int deleteNotice(@Param("noticeId") Long noticeId, @Param("receiverId") Long receiverId);
}
