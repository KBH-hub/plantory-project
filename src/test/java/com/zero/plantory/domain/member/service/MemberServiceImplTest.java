package com.zero.plantory.domain.member.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.zero.plantory.domain.member.mapper.MemberMapper;
import com.zero.plantory.global.vo.MemberVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@Slf4j
class MemberServiceImplTest {

    @Mock
    private MemberMapper memberMapper;

    @InjectMocks
    private MemberServiceImpl memberService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("아이디 중복이 존재하면 true 반환")
    void testIsDuplicateMembernameTrue() {
        when(memberMapper.countByMembername("testUser")).thenReturn(1);

        boolean result = memberService.isDuplicateMembername("testUser");

        assertTrue(result);
    }

    @Test
    @DisplayName("아이디 중복이 없으면 false 반환")
    void testIsDuplicateMembernameFalse() {
        when(memberMapper.countByMembername("testUser")).thenReturn(0);

        boolean result = memberService.isDuplicateMembername("testUser");

        assertFalse(result);
    }

    @Test
    @DisplayName("닉네임 중복이 존재하면 true 반환")
    void testIsDuplicateNicknameTrue() {
        when(memberMapper.countByNickname("nick")).thenReturn(1);

        boolean result = memberService.isDuplicateNickname("nick");

        assertTrue(result);
    }

    @Test
    @DisplayName("닉네임 중복이 없으면 false 반환")
    void testIsDuplicateNicknameFalse() {
        when(memberMapper.countByNickname("nick")).thenReturn(0);

        boolean result = memberService.isDuplicateNickname("nick");

        assertFalse(result);
    }

    @Test
    @DisplayName("회원가입 성공")
    void testSignUpSuccess() {
        MemberVO member = new MemberVO();
        member.setMembername("aaa");
        member.setNickname("aaa");

        when(memberMapper.countByMembername("aaa")).thenReturn(0);
        when(memberMapper.countByNickname("aaa")).thenReturn(0);
        when(memberMapper.insertMember(member)).thenReturn(1);

        boolean result = memberService.signUp(member);

        assertTrue(result);
    }

    @Test
    @DisplayName("회원가입 실패 - 아이디 중복 오류")
    void testSignUpDuplicateMembername() {
        MemberVO member = new MemberVO();
        member.setMembername("aaa");
        member.setNickname("aaa");

        when(memberMapper.countByMembername("aaa")).thenReturn(1);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> memberService.signUp(member));

        assertEquals("이미 사용 중인 아이디입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("회원가입 실패 - 닉네임 중복 오류")
    void testSignUpDuplicateNickname() {
        MemberVO member = new MemberVO();
        member.setMembername("aaa");
        member.setNickname("aaa");

        when(memberMapper.countByMembername("aaa")).thenReturn(0);
        when(memberMapper.countByNickname("aaa")).thenReturn(1);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> memberService.signUp(member));

        assertEquals("이미 사용 중인 닉네임입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("로그인 성공 - 정지 없음")
    void testLoginSuccess() {
        MemberVO member = new MemberVO();
        member.setMembername("aaa");
        member.setStopDay(null);

        when(memberMapper.selectByMembername("aaa")).thenReturn(member);

        MemberVO result = memberService.login("aaa", "1234");

        assertNotNull(result);
    }

    @Test
    @DisplayName("로그인 실패 - 정지된 회원")
    void testLoginBlocked() {
        MemberVO member = new MemberVO();
        member.setStopDay(LocalDateTime.now().plusDays(5));

        when(memberMapper.selectByMembername("aaa")).thenReturn(member);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> memberService.login("aaa", "1234"));

        assertTrue(exception.getMessage().contains("정지 해제까지"));
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 계정")
    void testLoginUserNotFound() {
        when(memberMapper.selectByMembername("aaa")).thenReturn(null);

        MemberVO result = memberService.login("aaa", "1234");

        assertNull(result);
    }
}
