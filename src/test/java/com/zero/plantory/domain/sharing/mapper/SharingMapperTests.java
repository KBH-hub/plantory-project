package com.zero.plantory.domain.sharing.mapper;

import com.zero.plantory.domain.sharing.vo.SharingPopularVO;
import com.zero.plantory.domain.sharing.vo.SharingSearchVO;
import com.zero.plantory.domain.sharing.vo.SharingVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SharingMapperTests {

    @Autowired
    SharingMapper mapper;

    private final Long memberId = 1L;
    private final Long sharingId = 20L;

    @Test
    @DisplayName("관심 등록")
    void insertInterestTest() {

        int before = mapper.countInterest(memberId, sharingId);
        log.info("등록 전 관심 여부 = {}", before);

        if (before == 0) {
            mapper.insertInterest(memberId, sharingId);
            mapper.increaseInterestNum(sharingId);
        }

        int after = mapper.countInterest(memberId, sharingId);
        log.info("등록 후 관심 여부 = {}", after);

    }

    @Test
    @DisplayName("관심 해제")
    void deleteInterestTest() {

        int before = mapper.countInterest(memberId, sharingId);
        log.info("해제 전 관심 여부 = {}", before);

        if (before == 1) {
            mapper.deleteInterest(memberId, sharingId);
            mapper.decreaseInterestNum(sharingId);
        }

        int after = mapper.countInterest(memberId, sharingId);
        log.info("해제 후 관심 여부 = {}", after);
    }

    @Test
    @DisplayName("나눔 게시글 상세 댓글 조회")
    void selectSharingCommentsTest(){
        log.info("selectSharingComments = {}", mapper.selectSharingComments(2L));
    }

    @Test
    @DisplayName("나눔 게시글 상세조회")
    void selectSharingDetailTest() {
        log.info("sharingDetail = {}", mapper.selectSharingDetail(2L));

    }

    @Test
    @DisplayName("나눔 게시글 등록 처리")
    void insertSharingTest() {
        SharingVO vo = SharingVO.builder()
                .memberId(1L)
                .title("테스트 제목")
                .content("테스트 내용")
                .plantType("금전수")
                .managementLevel("쉬움")
                .managementNeeds("약간 돌봄")
                .status("false")
                .build();

        mapper.insertSharing(vo);
        log.info("생성된 sharing_id = {}", vo.getSharingId());
    }

    @Test
    @DisplayName("인기 나눔글 TOP3 조회")
    void selectPopularSharingListTest() {
        mapper.selectPopularSharingList()
                .forEach(vo -> log.info(vo.toString()));
    }

    @Test
    @DisplayName("내 관심 나눔 식물 수 조회")
    void countInterestByMemberIdTest(){
        log.info("interestPlants = {}", mapper.countInterestByMemberId(1L));
    }

    @Test
    @DisplayName("나눔식물 조회 및 검색")
    void selectSharingListByAddressAndKeywordTest() {
        SharingSearchVO vo = SharingSearchVO.builder()
                .userAddress("서울특별시 금천구")
                .keyword("")   // 검색어 없음 → 전체조회
                .limit(10)
                .offset(0)
                .build();

        mapper.selectSharingListByAddressAndKeyword(vo)
                .forEach(item -> log.info(item.toString()));

    }



}
