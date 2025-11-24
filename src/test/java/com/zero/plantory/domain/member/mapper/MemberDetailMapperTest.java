package com.zero.plantory.domain.member.mapper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.zero.plantory.domain.member.vo.MemberUpdateRequestVO;
import com.zero.plantory.global.vo.MemberVO;
import com.zero.plantory.global.vo.Role;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class MemberDetailMapperTest {

    @Autowired
    MemberMapper memberMapper;

    @Test
    @DisplayName("회원가입 처리")
    void insertMemberTest() {
        MemberVO member = MemberVO.builder()
                .membername("testUser")
                .password("pw1234!")
                .nickname("테스트유저")
                .phone("010-1234-5678")
                .address("서울특별시 영등포구")
                .role(Role.USER)
                .noticeEnabled(1)
                .build();

        int result = memberMapper.insertMember(member);
        assertTrue(result > 0);
    }

    @Test
    @DisplayName("아이디 중복 확인")
    void countByMembernameTest() {
        int count = memberMapper.countByMembername("honggd01");
        log.info("count = {}", count);
        assertTrue(count >= 0);
    }

    @Test
    @DisplayName("닉네임 중복 확인")
    void countByNicknameTest() {
        int count = memberMapper.countByNickname("길동가든");
        assertTrue(count >= 0);
    }

}
