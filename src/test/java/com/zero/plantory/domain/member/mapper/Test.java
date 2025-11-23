//package com.zero.plantory.domain.member.mapper;
//
//import com.zero.plantory.domain.member.vo.MemberInfoVO;
//import com.zero.plantory.domain.member.vo.MemberUpdateRequestVO;
//import com.zero.plantory.global.vo.MemberVO;
//import com.zero.plantory.global.vo.Role;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.MethodOrderer;
//import org.junit.jupiter.api.TestMethodOrder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@Slf4j
//@SpringBootTest
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//class Test {
//
//    @Autowired MemberMapper memberMapper;
//    @Autowired MyContentMapper myContentMapper;
//    @Autowired MyProfileMapper myProfileMapper;
//    @Autowired MySharingHistoryMapper sharingHistoryMapper;
//
//    @org.junit.jupiter.api.Test
//    @DisplayName("아이디 중복 확인")
//    void countByMembernameTest() {
//        log.info("count = {}", memberMapper.countByMembername("honggd01"));
//    }
//
//    @org.junit.jupiter.api.Test
//    @DisplayName("닉네임 중복 확인")
//    void countByNicknameTest() {
//        log.info("count = {}", memberMapper.countByNickname("길동가든"));
//    }
//
//    @org.junit.jupiter.api.Test
//    @DisplayName("회원가입 처리")
//    void insertMemberTest() {
//        MemberVO memberVo = MemberVO.builder()
//                .membername("test01")
//                .password("ms2024!")
//                .nickname("식집사테스트")
//                .phone("010-1111-2222")
//                .address("서울특별시 금천구")
//                .role(Role.USER)
//                .noticeEnabled(1)
//                .build();
//
//        assertTrue(memberMapper.insertMember(memberVo) > 0);
//    }
//
//    @org.junit.jupiter.api.Test
//    @DisplayName("로그인 처리")
//    void selectByMembernameTest() {
//        MemberVO result = memberMapper.selectByMembername("honggd01");
//        assertNotNull(result);
//        log.info(result.toString());
//    }
//
//    @org.junit.jupiter.api.Test
//    @DisplayName("내 정보 조회")
//    void selectMyInfoTest() {
//        MemberInfoVO result = myProfileMapper.selectMyInfo(20L);
//        assertNotNull(result);
//        log.info(result.toString());
//    }
//
//    @org.junit.jupiter.api.Test
//    @DisplayName("내 정보 수정 처리")
//    void updateMyInfoTest() {
//        MemberUpdateRequestVO request = MemberUpdateRequestVO.builder()
//                .memberId(20L)
//                .nickname("호준그린2")
//                .phone("010-7492-0000")
//                .address("서울특별시 구로구")
//                .build();
//
//        assertTrue(memberMapper.updateMyInfo(request) > 0);
//    }
//
//    @org.junit.jupiter.api.Test
//    @DisplayName("알림 설정 변경")
//    void updateNoticeEnabledTest() {
//        assertTrue(myProfileMapper.updateNoticeEnabled(2L, 0) > 0);
//    }
//
//    @org.junit.jupiter.api.Test
//    @DisplayName("회원 탈퇴 처리")
//    void deleteMemberTest() {
//        assertTrue(myProfileMapper.deleteMember(2L) > 0);
//    }
//
//    @org.junit.jupiter.api.Test
//    @DisplayName("나눔 내역 카운트 조회")
//    void countByInterestCount() {
//        log.info("interest count = {}", sharingHistoryMapper.countByInterestCount(1L));
//    }
//
//    @org.junit.jupiter.api.Test
//    @DisplayName("공감내역 수 확인")
//    void countByCompletedSharingCount() {
//        log.info("completed sharing count = {}", sharingHistoryMapper.countByCompletedSharingCount(1L));
//    }
//}
