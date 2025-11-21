package com.zero.plantory.domain.member.mapper;

import com.zero.plantory.domain.member.dto.request.MyWrittenListRequestVO;
import com.zero.plantory.domain.member.vo.MyWrittenListVO;
import com.zero.plantory.global.vo.MemberVO;
import com.zero.plantory.global.vo.Role;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
        memberMapper.selectByMembername("test01");

        log.info(String.valueOf(memberMapper.selectByMembername("test01")));
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
    void deleteMyWrittenAll() {
    }

    @Test
    void deleteMyWrittenSharing() {
    }

    @Test
    void deleteMyWrittenQuestion() {
    }
}