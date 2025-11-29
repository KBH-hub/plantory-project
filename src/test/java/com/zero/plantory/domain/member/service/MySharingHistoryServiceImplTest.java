package com.zero.plantory.domain.member.service;

import com.zero.plantory.domain.myProfile.dto.MySharingHistoryListRequest;
import com.zero.plantory.domain.myProfile.dto.MySharingHistoryListResponse;
import com.zero.plantory.domain.myProfile.mapper.MySharingHistoryMapper;
import com.zero.plantory.domain.myProfile.service.MySharingHistoryServiceImpl;
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
class MySharingHistoryServiceImplTest {

    @Mock
    private MySharingHistoryMapper mySharingHistoryMapper;

    @InjectMocks
    private MySharingHistoryServiceImpl mySharingHistoryService;

    @Test
    @DisplayName("나눔 관심 수 조회 테스트")
    void getInterestCountTest() {
        when(mySharingHistoryMapper.countByInterestCount(1L)).thenReturn(5);

        int result = mySharingHistoryService.getInterestCount(1L);

        assertEquals(5, result);
        verify(mySharingHistoryMapper, times(1)).countByInterestCount(1L);
    }

    @Test
    @DisplayName("완료된 나눔 수 조회 테스트")
    void getCompletedSharingCountTest() {
        when(mySharingHistoryMapper.countByCompletedSharingCount(1L)).thenReturn(3);

        int result = mySharingHistoryService.getCompletedSharingCount(1L);

        assertEquals(3, result);
        verify(mySharingHistoryMapper, times(1)).countByCompletedSharingCount(1L);
    }

    @Test
    @DisplayName("나의 나눔 내역 리스트 조회 테스트")
    void getMySharingHistoryListTest() {
        MySharingHistoryListRequest request = new MySharingHistoryListRequest();
        request.setMemberId(1L);

        MySharingHistoryListResponse item = new MySharingHistoryListResponse();
        item.setSharingId(10L);
        item.setTitle("나눔 테스트 제목");

        when(mySharingHistoryMapper.selectMySharingList(request)).thenReturn(List.of(item));

        List<MySharingHistoryListResponse> result = mySharingHistoryService.getMySharingHistoryList(request);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("나눔 테스트 제목", result.get(0).getTitle());
        verify(mySharingHistoryMapper, times(1)).selectMySharingList(request);
    }
}
