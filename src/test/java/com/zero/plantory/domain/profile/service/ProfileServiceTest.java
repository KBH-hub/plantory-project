package com.zero.plantory.domain.profile.service;

import com.zero.plantory.domain.member.mapper.MemberMapper;
import com.zero.plantory.domain.member.service.MemberService;
import com.zero.plantory.domain.profile.dto.MemberResponse;
import com.zero.plantory.domain.profile.dto.MemberSignUpRequest;
import com.zero.plantory.domain.profile.mapper.ProfileMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Slf4j
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProfileServiceTest {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileMapper profileMapper;

    @Autowired
    private BCryptPasswordEncoder  bCryptPasswordEncoder;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private MemberService memberService;

    private Long testMemberId;

    @BeforeAll
    void setupMember() {
        MemberSignUpRequest memberSignUpRequest = new MemberSignUpRequest();
        memberSignUpRequest.setMembername("testUser8581");
        memberSignUpRequest.setPassword("oldPassword123!");
        memberSignUpRequest.setNickname("testUser8581");
        memberSignUpRequest.setPhone("010-8789-9999");
        memberSignUpRequest.setAddress("서울특별시 금천구");

        memberService.signUp(memberSignUpRequest);

        MemberResponse memberResponse = memberMapper.selectByMembername("testUser8581");
        testMemberId = memberResponse.getMemberId();

        log.info("테스트용 회원 생성 완료, memberId={}", testMemberId);
    }

    @Test
    @DisplayName("새 비밀번호로 변경")
    void changePasswordTest() {
        // 비밀번호 변경
        boolean result = profileService.changePassword(testMemberId, "oldPassword123!", "newPassword456!");
        log.info("changePassword 결과: {}", result);

        assertThat(result).isTrue();

        // 변경된 비밀번호 확인
        MemberResponse updated = profileMapper.selectByMemberId(testMemberId);
        log.info("DB 저장 비밀번호: {}", updated.getPassword());

        assertThat(bCryptPasswordEncoder.matches("oldPassword123!", updated.getPassword())).isFalse();
        assertThat(bCryptPasswordEncoder.matches("newPassword456!", updated.getPassword())).isTrue();
    }

    @AfterAll
    void cleanupMember() {
        // 테스트용 회원 삭제
        profileMapper.deleteMember(testMemberId);
        log.info("테스트용 회원 삭제 완료, memberId={}", testMemberId);
    }
}