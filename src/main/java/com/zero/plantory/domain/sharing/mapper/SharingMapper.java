package com.zero.plantory.domain.sharing.mapper;

import com.zero.plantory.domain.sharing.vo.*;
import com.zero.plantory.global.vo.CommentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SharingMapper {
    List<SharingCardListVO> selectSharingListByAddressAndKeyword(SharingSearchVO vo);
    int countInterestByMemberId(Long memberId);
    List<SharingPopularVO> selectPopularSharingList();
    int insertSharing(SharingVO vo);
    SelectSharingDetailVO selectSharingDetail(Long sharingId);
    List<SelectSharingCommentVO> selectSharingComments(Long sharingId);


}
