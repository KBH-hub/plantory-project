package com.zero.plantory.domain.member.mapper;

import com.zero.plantory.domain.member.dto.request.MyWrittenDeleteRequestVO;
import com.zero.plantory.domain.member.dto.request.MyWrittenListRequestVO;
import com.zero.plantory.global.vo.MemberVO;
import com.zero.plantory.global.vo.Role;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MemberMapperTest {

    @Autowired
    MemberMapper memberMapper;

    @Test
    @DisplayName("아이디 중복 확인")
    void countByMembernameTest() {
        MemberVO memberVo = new MemberVO();
        memberVo.setMembername("honggd01");

        log.info(String.valueOf(memberMapper.countByMembername(memberVo.getMembername())));
    }

    @Test
    @DisplayName("닉네임 중복 확인")
    void countByNicknameTest() {
        MemberVO memberVo = new MemberVO();
        memberVo.setNickname("길동가든");

        log.info(String.valueOf(memberMapper.countByNickname(memberVo.getNickname())));
    }

    @Test
    @DisplayName("회원가입 처리")
    void insertMemberTest() {
            MemberVO memberVo = MemberVO.builder()
                    .membername("test01")
                    .password("ms2024!")
                    .nickname("식집사테스트")
                    .phone("010-1111-2222")
                    .address("서울특별시 금천구")
                    .role(Role.USER)
                    .noticeEnabled(1)
                    .build();

            log.info(String.valueOf(memberMapper.insertMember(memberVo)));
    }

    @Test
    @DisplayName("로그인 처리")
    void selectByMembernameTest() {
        memberMapper.selectByMembername("honggd01");

        log.info(String.valueOf(memberMapper.selectByMembername("honggd01")));
    }



    @Test
    @DisplayName("내프로필정보확인")
    void selectByMemberInfoTest() {
        log.info(String.valueOf(memberMapper.selectByMemberInfo(1L)));
    }

    @Test
    @DisplayName("내프로필정보(나눔지수)확인")
    void countByInterestCount() {
        log.info(String.valueOf(memberMapper.countByInterestCount(1L)));
    }
    //log.info(String.valueOf());
    @Test
    @DisplayName("공감내역 수 확인")
    void countByCompletedSharingCount() {
        log.info(String.valueOf(memberMapper.countByCompletedSharingCount(1L)));
    }

    @Test
    @DisplayName("내가 쓴 글 확인 - 전체")
    void selectMyWrittenListTest() {
        MyWrittenListRequestVO request = new MyWrittenListRequestVO();
        request.setMemberId(1L);
        request.setKeyword("분양");
        request.setLimit(10);
        request.setOffset(0);

        log.info(String.valueOf(memberMapper.selectMyWrittenListAll(request)));
    }


    @Test
    void selectMyWrittenListSharing() {
        MyWrittenListRequestVO request = new MyWrittenListRequestVO();
        request.setMemberId(1L);
        request.setKeyword("분양");
        request.setLimit(10);
        request.setOffset(0);

        log.info(String.valueOf(memberMapper.selectMyWrittenListSharing(request)));
    }

    @Test
    void selectMyWrittenListQuestion() {
        MyWrittenListRequestVO request = new MyWrittenListRequestVO();
        request.setMemberId(1L);
        request.setKeyword("초록");
        request.setLimit(10);
        request.setOffset(0);

        log.info(String.valueOf(memberMapper.selectMyWrittenListQuestion(request)));
    }

    @Test
    @DisplayName("내가쓴글 체크박스 체크된 나눔글 삭제")
    void deleteMyWrittenSharingTest() {
        MyWrittenDeleteRequestVO request = MyWrittenDeleteRequestVO.builder()
                .memberId(1L)
                .sharingIds(Arrays.asList(1L, 2L, 3L))
                .build();

        memberMapper.deleteMyWrittenSharing(request);

    }

    @Test
    @DisplayName("내가쓴글 체크박스 체크된 질문글 삭제")
    void deleteMyWrittenQuestionTest() {
        MyWrittenDeleteRequestVO request = MyWrittenDeleteRequestVO.builder()
                .memberId(1L)
                .questionIds(Arrays.asList(4L, 5L, 6L))
                .build();

        memberMapper.deleteMyWrittenQuestion(request);
    }

    @Test
    @DisplayName("댓글 단 글 검색 - 전체")
    void selectMyCommentSearchAll() {
        MyWrittenListRequestVO request = MyWrittenListRequestVO.builder()
                .memberId(1L)
                .keyword("드려요")   // 예: 검색 키워드
                .limit(10)
                .offset(0)
                .build();

        var result = memberMapper.selectMyCommentSearchAll(request);

        assertNotNull(result);
        assertFalse(result.isEmpty(), "검색 결과 없음");
        result.forEach(item -> log.info(item.toString()));
    }

    @Test
    @DisplayName("댓글 단 글 검색 - 나눔글")
    void selectMyCommentSearchSharing() {
        MyWrittenListRequestVO request = MyWrittenListRequestVO.builder()
                .memberId(1L)
                .keyword("드려요")
                .limit(10)
                .offset(0)
                .build();

        var result = memberMapper.selectMyCommentSearchSharing(request);

        assertNotNull(result);
        result.forEach(item -> log.info(item.toString()));
    }

    @Test
    @DisplayName("댓글 단 글 검색 - 질문글")
    void selectMyCommentSearchQuestion() {
        MyWrittenListRequestVO request = MyWrittenListRequestVO.builder()
                .memberId(1L)
                .keyword("꽃")
                .limit(10)
                .offset(0)
                .build();

        var result = memberMapper.selectMyCommentSearchQuestion(request);

        assertNotNull(result);
        result.forEach(item -> log.info(item.toString()));
    }
}