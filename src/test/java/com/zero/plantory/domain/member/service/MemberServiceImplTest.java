package com.zero.plantory.domain.member.service;

import com.zero.plantory.domain.member.mapper.MemberMapper;
import com.zero.plantory.global.vo.MemberVO;
import com.zero.plantory.global.vo.Role;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MemberServiceImplTest {

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private MemberService memberService;

    @Test
    void isDuplicateMembernameTest() {
        MemberVO memberVo = new MemberVO();
        memberVo.setMembername("honggd01");

        boolean result = memberService.isDuplicateMembername(memberVo.getMembername());
        log.info("isDuplicateMembernameTest is duplicate membername: {}", memberVo.getMembername());
        assertTrue(result);
    }

    @Test
    void isDuplicateNicknameTest() {
        MemberVO memberVo = new MemberVO();
        memberVo.setNickname("길동가든");

        boolean result = memberService.isDuplicateNickname(memberVo.getNickname());
        log.info("isDuplicateNicknameTest is duplicate nickname: {}", memberVo.getNickname());
        assertTrue(result);
    }

    @Test
    void isInsertMemberTest() {
        MemberVO memberVo = MemberVO.builder()
                .membername("test01")
                .password("ms2024!")
                .nickname("식집사테스트")
                .phone("010-1111-2222")
                .address("서울특별시 금천구")
                .role(Role.USER)
                .noticeEnabled(1)
                .build();

        boolean result = memberService.signUpMember(memberVo);
        log.info("isInsertMemberTest is insert member: {}", memberVo);
        assertTrue(result);
    }

}