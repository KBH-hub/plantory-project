package com.zero.plantory.domain.member.service;

import com.zero.plantory.domain.profile.dto.ProfileInfoResponse;
import com.zero.plantory.domain.profile.dto.MemberUpdateRequest;
import com.zero.plantory.domain.profile.mapper.ProfileMapper;
import com.zero.plantory.domain.profile.service.ProfileServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceImplTest {

    @Mock
    private ProfileMapper profileMapper;

    @InjectMocks
    private ProfileServiceImpl profileService;

    @Test
    @DisplayName("내 정보 조회 테스트")
    void getProfileInfoTest() {
        Long memberId = 1L;
        ProfileInfoResponse myInfo = new ProfileInfoResponse();
        myInfo.setMemberId(memberId);
        myInfo.setNickname("테스트닉");

        when(profileMapper.selectProfileInfo(memberId)).thenReturn(myInfo);

        ProfileInfoResponse result = profileService.getProfileInfo(memberId);

        assertNotNull(result);
        assertEquals("테스트닉", result.getNickname());
        verify(profileMapper, times(1)).selectProfileInfo(memberId);
    }

    @Test
    @DisplayName("알림 설정 변경 테스트")
    void updateNoticeEnabledTest() {
        when(profileMapper.updateNoticeEnabled(1L, 1)).thenReturn(1);

        boolean result = profileService.updateNoticeEnabled(1L, 1);

        assertTrue(result);
        verify(profileMapper, times(1)).updateNoticeEnabled(1L, 1);
    }

    @Test
    @DisplayName("회원 정보 수정 테스트")
    void updateMyInfoTest() {
        MemberUpdateRequest request = MemberUpdateRequest.builder()
                .memberId(1L)
                .nickname("새닉네임")
                .phone("010-1234-5678")
                .build();

        when(profileMapper.updateProfileInfo(request)).thenReturn(1);

        boolean result = profileService.updateMyInfo(request);

        assertTrue(result);
        verify(profileMapper, times(1)).updateProfileInfo(request);
    }

    @Test
    @DisplayName("회원 탈퇴 테스트")
    void deleteMemberTest() {
        when(profileMapper.deleteMember(1L)).thenReturn(1);

        boolean result = profileService.deleteMember(1L);

        assertTrue(result);
        verify(profileMapper, times(1)).deleteMember(1L);
    }
}
