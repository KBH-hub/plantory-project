package com.zero.plantory.domain.sharing.mapper;

import com.zero.plantory.domain.sharing.dto.CommentRequest;
import com.zero.plantory.domain.sharing.dto.SharingHistoryResponse;
import com.zero.plantory.domain.sharing.dto.SharingRequest;
import com.zero.plantory.domain.sharing.dto.SharingSearchRequest;
import com.zero.plantory.global.dto.ManageLevel;
import com.zero.plantory.global.dto.ManageDemand;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.apache.commons.lang3.BooleanUtils.forEach;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
    @DisplayName("후기작성 - 내가 나눔한 기록 조회")
    void selectProfileSharingGivenTest() {
        mapper.selectProfileSharingGiven(1L)
                .forEach(item ->
                log.info("나눔한 기록 → id={}, 상대={}, 제목={}",
                        item.getSharingId(),
                        item.getPartnerNickname(),
                        item.getTitle())
        );
    }

    @Test
    @DisplayName("후기 작성 - 내가 나눔받은 기록 조회")
    void selectProfileSharingReceivedTest() {
        mapper.selectProfileSharingReceived(1L)
                .forEach(item ->
                log.info("나눔받은 기록 → id={}, 상대={}, 제목={}",
                        item.getSharingId(),
                        item.getPartnerNickname(),
                        item.getTitle())
        );
    }

    @Test
    @DisplayName("나눔지수 업데이트")
    void updateSharingRateTest() {
        log.info("나눔지수 업데이트 ={}", mapper.updateSharingRate(1L, BigDecimal.valueOf(1.50)));
    }

    @Test
    @DisplayName("나눔 완료")
    void updateSharingCompleteTest() {
        log.info("나눔 완료 처리 결과 (영향받은 행 수) = {}",  mapper.updateSharingComplete(19L, 20L));
    }

    @Test
    @DisplayName("나눔상대 조회")
    void selectSharingMessagePartnersTest() {
        mapper.selectSharingMessagePartners(12L, 19L)
                        .forEach(user -> log.info("요청 보낸 사용자 = {}({})",
                                user.getNickname(), user.getMemberId()));
    }

    @Test
    @DisplayName("나눔 게시글 삭제")
    void deleteSharingTest() {
        log.info("삭제 결과 = {}", mapper.deleteSharing(12L));
        log.info("삭제 후 존재 여부(0이면 삭제됨) = {}", mapper.countProfileSharing(12L, 1L));
    }

    @Test
    @DisplayName("나눔글 수정 권한 체크")
    void countProfileSharingTest() {
        int count = mapper.countProfileSharing(12L, 1L);
        log.info("수정 권한 여부 = {}", count);
    }

    @Test
    @DisplayName("나눔글 내용 수정")
    void updateSharingTest() {

        SharingRequest request = SharingRequest.builder()
                .sharingId(12L)
                .memberId(1L)
                .title("산세베리아 나눔해요 (시간 조율 가능)")
                .content("튼튼한 산세베리아 나눔합니다. 평일 저녁이나 주말 가능해요.")
                .plantType("산세베리아 골든 하니")
                .managementLevel(ManageLevel.EASY)
                .managementNeeds(ManageDemand.LITTLE_CARE)
                .build();

        log.info("내용 수정 결과 = {}", mapper.updateSharing(request));
    }

    @Test
    @DisplayName("댓글 삭제")
    void deleteCommentTest() {

        CommentRequest request = CommentRequest.builder()
                .commentId(21L)
                .sharingId(20L)
                .writerId(1L)
                .build();

        int count = mapper.countProfileComment(request.getCommentId(), request.getSharingId(), request.getWriterId());
        log.info("삭제 권한 여부 = {}", count);

        if (count == 1) {
            int result = mapper.deleteComment(request);
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

        int count = mapper.countProfileComment(commentId, sharingId, writerId);
        log.info("수정 권한 여부 = {}", count);

        if (count == 1) {
            CommentRequest request = CommentRequest.builder()
                    .commentId(21L)
                    .sharingId(20L)
                    .writerId(1L)
                    .content("수정된 댓글입니다.")
                    .build();

            log.info("댓글 수정 결과 = {}", mapper.updateCommentById(request));
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
        log.info("readSharingDetail = {}", mapper.selectSharingDetail(2L));

    }

    @Test
    @DisplayName("나눔 게시글 등록 처리")
    void insertSharingTest() {
        SharingRequest request = SharingRequest.builder()
                .memberId(1L)
                .title("테스트 제목")
                .content("테스트 내용")
                .plantType("금전수")
                .managementLevel(ManageLevel.EASY)
                .managementNeeds(ManageDemand.LITTLE_CARE)
                .status("false")
                .build();

        mapper.insertSharing(request);
        log.info("생성된 sharing_id = {}", request.getSharingId());
    }

    @Test
    @DisplayName("인기 나눔글 TOP3 조회")
    void selectPopularSharingListTest() {
        SharingSearchRequest request = new SharingSearchRequest();
        request.setUserAddress(null);

        mapper.selectPopularSharingList(request)
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
        SharingSearchRequest vo = SharingSearchRequest.builder()
                .userAddress("서울특별시 금천구")
                .keyword("")   // 검색어 없음 → 전체조회
                .limit(10)
                .offset(0)
                .build();

        mapper.selectSharingListByAddressAndKeyword(vo)
                .forEach(item -> log.info(item.toString()));

    }

    @Test
    @DisplayName("분양자 리뷰정보 조회")
    void selectReviewInfoForGiverTest() {
        Long sharingId = 1L;
        Long loginMemberId = 10L;

        log.info("selectReviewInfoForGive = {}", mapper.selectReviewInfoForGiver(sharingId, loginMemberId));

    }

    @Test
    @DisplayName("피분양자 리뷰정보 조회")
    void selectReviewInfoForReceiverTest() {
        Long sharingId = 1L;
        Long loginMemberId = 20L;

        log.info("selectReviewInfoForReceiver = {}", mapper.selectReviewInfoForReceiver(sharingId, loginMemberId));
    }



}
