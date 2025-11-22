package com.zero.plantory.domain.sharing.mapper;

import com.zero.plantory.domain.sharing.vo.SharingPopularVO;
import com.zero.plantory.domain.sharing.vo.SharingSearchVO;
import com.zero.plantory.global.vo.*;
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
    private final Long writerId = 1L;

    @Test
    @DisplayName("나눔글 수정 권한 체크")
    void countMySharingTest() {
        int count = mapper.countMySharing(12L, 1L);
        log.info("수정 권한 여부 = {}", count);
    }

    @Test
    @DisplayName("나눔글 내용 수정")
    void updateSharingTest() {

        SharingVO vo = SharingVO.builder()
                .sharingId(12L)
                .memberId(1L)
                .title("산세베리아 나눔해요 (시간 조율 가능)")
                .content("튼튼한 산세베리아 나눔합니다. 평일 저녁이나 주말 가능해요.")
                .plantType("산세베리아 골든 하니")
                .managementLevel(ManagementLevel.EASY)
                .managementNeeds(ManagementNeeds.LITTLE_CARE)
                .build();

        log.info("내용 수정 결과 = {}", mapper.updateSharing(vo));
    }

    @Test
    @DisplayName("나눔글 이미지 삭제")
    void deleteSharingImageTest() {
        log.info("이미지 삭제 결과 = {}",  mapper.deleteSharingImage(ImageTargetType.SHARING, 12L, 1L));
    }


    @Test
    @DisplayName("나눔글 이미지 등록")
    void insertSharingImageTest() {

        ImageVO img = ImageVO.builder()
                .memberId(1L)
                .targetType(ImageTargetType.SHARING)
                .targetId(12L)
                .fileUrl("https://storage.googleapis.com/plantory/images/2025/11/20/sharing54_new.jpg")
                .fileName("sharing5_new.jpg")
                .build();

        log.info("이미지 등록 결과 = {}",  mapper.insertSharingImage(img));
    }

    @Test
    @DisplayName("댓글 삭제")
    void deleteCommentTest() {

        CommentVO vo = CommentVO.builder()
                .commentId(21L)
                .sharingId(20L)
                .writerId(1L)
                .build();

        int count = mapper.countMyComment(vo.getCommentId(), vo.getSharingId(), vo.getWriterId());
        log.info("삭제 권한 여부 = {}", count);

        if (count == 1) {
            int result = mapper.deleteComment(vo);
            log.info("삭제 결과 = {}", result);
        }
    }

    @Test
    @DisplayName("댓글 등록")
    void insertCommentTest() {

        int result = mapper.insertComment(sharingId, writerId, "테스트 댓글입니다!");
        log.info("댓글 등록 결과 = {}", result);
    }

    @Test
    @DisplayName("댓글 수정")
    void updateCommentTest() {

        Long commentId = 1L;

        int count = mapper.countMyComment(commentId, sharingId, writerId);
        log.info("수정 권한 여부 = {}", count);

        if (count == 1) {
            CommentVO vo = CommentVO.builder()
                    .commentId(21L)
                    .sharingId(20L)
                    .writerId(1L)
                    .content("수정된 댓글입니다.")
                    .build();

            log.info("댓글 수정 결과 = {}", mapper.updateCommentById(vo));
        }

    }

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
                .managementLevel(ManagementLevel.EASY)
                .managementNeeds(ManagementNeeds.LITTLE_CARE)
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
