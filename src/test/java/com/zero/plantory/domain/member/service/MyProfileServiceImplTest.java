package com.zero.plantory.domain.member.service;

import com.zero.plantory.domain.member.mapper.MyProfileMapper;
import com.zero.plantory.domain.member.vo.MemberInfoVO;
import com.zero.plantory.domain.member.vo.MemberUpdateRequestVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MyprofileServiceImplTest {

    @Mock
    private MyProfileMapper myProfileMapper;

    @InjectMocks
    private MyprofileServiceImpl myProfileService;

    @Test
    @DisplayName("내 정보 조회 테스트")
    void getMyInfoTest() {
        Long memberId = 1L;
        MemberInfoVO myInfo = new MemberInfoVO();
        myInfo.setMemberId(memberId);
        myInfo.setNickname("테스트닉");

        when(myProfileMapper.selectMyInfo(memberId)).thenReturn(myInfo);

        MemberInfoVO result = myProfileService.getMyInfo(memberId);

        assertNotNull(result);
        assertEquals("테스트닉", result.getNickname());
        verify(myProfileMapper, times(1)).selectMyInfo(memberId);
    }

    @Test
    @DisplayName("알림 설정 변경 테스트")
    void updateNoticeEnabledTest() {
        when(myProfileMapper.updateNoticeEnabled(1L, 1)).thenReturn(1);

        boolean result = myProfileService.updateNoticeEnabled(1L, 1);

        assertTrue(result);
        verify(myProfileMapper, times(1)).updateNoticeEnabled(1L, 1);
    }

    @Test
    @DisplayName("회원 정보 수정 테스트")
    void updateMyInfoTest() {
        MemberUpdateRequestVO request = MemberUpdateRequestVO.builder()
                .memberId(1L)
                .nickname("새닉네임")
                .phone("010-1234-5678")
                .build();

        when(myProfileMapper.updateMyInfo(request)).thenReturn(1);

        boolean result = myProfileService.updateMyInfo(request);

        assertTrue(result);
        verify(myProfileMapper, times(1)).updateMyInfo(request);
    }

    @Test
    @DisplayName("회원 탈퇴 테스트")
    void deleteMemberTest() {
        when(myProfileMapper.deleteMember(1L)).thenReturn(1);

        boolean result = myProfileService.deleteMember(1L);

        assertTrue(result);
        verify(myProfileMapper, times(1)).deleteMember(1L);
    }
}
