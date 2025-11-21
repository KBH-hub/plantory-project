package com.zero.plantory.domain.member.mapper;

import com.zero.plantory.domain.member.vo.MemberVO;
import com.zero.plantory.domain.member.vo.Role;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MemberMapperTest {

    @Autowired
    MemberMapper memberMapper;

    @Test
    @DisplayName("아이디 중복 확인")
    void countByMembername() {
        MemberVO memberVo = new MemberVO();
        memberVo.setMembername("honggd01");

        log.info(String.valueOf(memberMapper.countByMembername(memberVo.getMembername())));
    }

    @Test
    @DisplayName("닉네임 중복 확인")
    void countByNickname() {
        MemberVO memberVo = new MemberVO();
        memberVo.setNickname("길동가든");

        log.info(String.valueOf(memberMapper.countByNickname(memberVo.getNickname())));
    }

    @Test
    @DisplayName("회원가입 처리")
    void insertMember() {
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
    void selectByMembername() {
        memberMapper.selectByMembername("test01");

        log.info(String.valueOf(memberMapper.selectByMembername("test01")));
    }
}