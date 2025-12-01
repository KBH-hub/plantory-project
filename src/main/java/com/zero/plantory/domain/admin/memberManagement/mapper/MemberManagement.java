package com.zero.plantory.domain.admin.memberManagement.mapper;

import com.zero.plantory.domain.profile.dto.MemberResponse;
import com.zero.plantory.global.dto.DeleteTargetType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemberManagement {
    List<MemberResponse> selectMemberList(@Param("keyword") String keyword, @Param("limit") int limit, @Param("offset") int offset);
    int deleteContent(@Param("targetType") DeleteTargetType targetType,@Param("targetId") Long targetId);
}
