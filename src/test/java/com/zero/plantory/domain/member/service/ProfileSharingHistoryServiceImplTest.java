package com.zero.plantory.domain.member.service;

import com.zero.plantory.domain.profile.dto.ProfileSharingHistoryListRequest;
import com.zero.plantory.domain.profile.dto.ProfileSharingHistoryListResponse;
import com.zero.plantory.domain.profile.mapper.ProfileSharingHistoryMapper;
import com.zero.plantory.domain.profile.service.ProfileSharingHistoryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileSharingHistoryServiceImplTest {

    @Mock
    private ProfileSharingHistoryMapper profileSharingHistoryMapper;

    @InjectMocks
    private ProfileSharingHistoryServiceImpl profileSharingHistoryService;

    @Test
    @DisplayName("나눔 관심 수 조회 테스트")
    void getInterestCountTest() {
        when(profileSharingHistoryMapper.countByInterestCount(1L)).thenReturn(5);

        int result = profileSharingHistoryService.getInterestCount(1L);

        assertEquals(5, result);
        verify(profileSharingHistoryMapper, times(1)).countByInterestCount(1L);
    }

    @Test
    @DisplayName("완료된 나눔 수 조회 테스트")
    void getCompletedSharingCountTest() {
        when(profileSharingHistoryMapper.countByCompletedSharingCount(1L)).thenReturn(3);

        int result = profileSharingHistoryService.getCompletedSharingCount(1L);

        assertEquals(3, result);
        verify(profileSharingHistoryMapper, times(1)).countByCompletedSharingCount(1L);
    }

    @Test
    @DisplayName("나의 나눔 내역 리스트 조회 테스트")
    void getProfileSharingHistoryListTest() {
        ProfileSharingHistoryListRequest request = new ProfileSharingHistoryListRequest();
        request.setMemberId(1L);

        ProfileSharingHistoryListResponse item = new ProfileSharingHistoryListResponse();
        item.setSharingId(10L);
        item.setTitle("나눔 테스트 제목");

        when(profileSharingHistoryMapper.selectProfileSharingList(request)).thenReturn(List.of(item));

        List<ProfileSharingHistoryListResponse> result = profileSharingHistoryService.getProfileSharingHistoryList(request).getList();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("나눔 테스트 제목", result.get(0).getTitle());
        verify(profileSharingHistoryMapper, times(1)).selectProfileSharingList(request);
    }
}
