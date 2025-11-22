package com.zero.plantory.domain.memberManagement.mapper;

import com.zero.plantory.global.vo.DeleteTargetType;
import com.zero.plantory.global.vo.MemberVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemberManagement {
    List<MemberVO> selectMemberList(@Param("keyword") String keyword, @Param("limit") int limit, @Param("offset") int offset);
    int deleteContent(@Param("targetType") DeleteTargetType targetType,@Param("targetId") Long targetId);
}
