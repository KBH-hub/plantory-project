package com.zero.plantory.domain.sharing.mapper;

import com.zero.plantory.domain.sharing.vo.SharingCardListVO;
import com.zero.plantory.domain.sharing.vo.SharingPopularVO;
import com.zero.plantory.domain.sharing.vo.SharingSearchVO;
import com.zero.plantory.domain.sharing.vo.SharingVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SharingMapper {
    List<SharingCardListVO> selectSharingListByAddressAndKeyword(SharingSearchVO vo);
    int countInterestByMemberId(Long memberId);
    List<SharingPopularVO> selectPopularSharingList();
    int insertSharing(SharingVO vo);
//    int insertSharingImage(ImageVO vo);


}
