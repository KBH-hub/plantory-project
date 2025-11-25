package com.zero.plantory.domain.member.service;

import com.zero.plantory.domain.member.dto.MemberSignUpRequest;
import com.zero.plantory.global.vo.Role;
import lombok.RequiredArgsConstructor;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@RequiredArgsConstructor
@Slf4j
class MemberDetailServiceImplTest {

    @Mock
    private MemberMapper memberMapper;

    @InjectMocks
    private MemberServiceImpl memberService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        memberService = new MemberServiceImpl(memberMapper, bCryptPasswordEncoder);
    }

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
    @DisplayName("회원가입 실패 - 아이디 중복 오류")
    void testSignUpDuplicateMembername() {
        // given
        MemberSignUpRequest request = MemberSignUpRequest.builder()
                .membername("aaa")
                .nickname("aaa")
                .password("12345678")
                .phone("01012345678")
                .address("서울")
                .build();

        when(memberMapper.countByMembername("aaa")).thenReturn(1);

        // when
        // then
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> memberService.signUp(request));

        assertEquals("이미 사용 중인 아이디입니다.", exception.getMessage());
    }


    @Test
    @DisplayName("회원가입 실패 - 닉네임 중복 오류")
    void testSignUpDuplicateNickname() {
        // given
        MemberSignUpRequest request = MemberSignUpRequest.builder()
                .membername("aaa")
                .nickname("aaa")
                .password("12345678")
                .phone("01012345678")
                .address("서울")
                .build();

        when(memberMapper.countByMembername("aaa")).thenReturn(0);
        when(memberMapper.countByNickname("aaa")).thenReturn(1);

        // when
        // then
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> memberService.signUp(request));

        assertEquals("이미 사용 중인 닉네임입니다.", exception.getMessage());
    }


    @Test
    @DisplayName("로그인 성공 - 정지 없음")
    void testLoginSuccess() {
        String password = "12345678";
        String encodedPassword = bCryptPasswordEncoder.encode(password);

        MemberVO memberVO = MemberVO.builder()
                .membername("hongTree")
                .password(encodedPassword)
                .nickname("홍나무")
                .build();

        when(memberMapper.selectByMembername("hongTree")).thenReturn(memberVO);

        MemberVO result = memberService.login("hongTree", "12345678");

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
