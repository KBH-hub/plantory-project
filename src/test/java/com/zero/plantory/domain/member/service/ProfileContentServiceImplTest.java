package com.zero.plantory.domain.member.service;

import com.zero.plantory.domain.profile.dto.ProfileWrittenDeleteRequest;
import com.zero.plantory.domain.profile.dto.ProfileWrittenListRequest;
import com.zero.plantory.domain.profile.dto.ProfileWrittenListResponse;
import com.zero.plantory.domain.profile.mapper.ProfileContentMapper;
import com.zero.plantory.domain.profile.service.ProfileContentServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileContentServiceImplTest {

    @Mock
    private ProfileContentMapper profileContentMapper;

    @InjectMocks
    private ProfileContentServiceImpl myContentService;

    @Test
    @DisplayName("내가 쓴 글 전체 조회 테스트")
    void getProfileWrittenListAllTest() {
        ProfileWrittenListRequest request = new ProfileWrittenListRequest();
        request.setMemberId(1L);

        ProfileWrittenListResponse item = new ProfileWrittenListResponse();
        item.setId(1L);
        item.setTitle("전체 글 테스트");

        when(profileContentMapper.selectProfileWrittenListAll(request)).thenReturn(List.of(item));

        List<ProfileWrittenListResponse> result = myContentService.getProfileWrittenListAll(request);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("전체 글 테스트", result.get(0).getTitle());
        verify(profileContentMapper, times(1)).selectProfileWrittenListAll(request);
    }

    @Test
    @DisplayName("내가 올린 나눔글 조회 테스트")
    void getProfileWrittenListSharingTest() {
        ProfileWrittenListRequest request = new ProfileWrittenListRequest();
        request.setMemberId(1L);

        ProfileWrittenListResponse item = new ProfileWrittenListResponse();
        item.setId(2L);
        item.setTitle("나눔글 테스트");

        when(profileContentMapper.selectProfileWrittenListSharing(request)).thenReturn(List.of(item));

        List<ProfileWrittenListResponse> result = myContentService.getProfileWrittenListSharing(request);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("나눔글 테스트", result.get(0).getTitle());
        verify(profileContentMapper, times(1)).selectProfileWrittenListSharing(request);
    }

    @Test
    @DisplayName("내가 쓴 질문글 조회 테스트")
    void getProfileWrittenListQuestionTest() {
        ProfileWrittenListRequest request = new ProfileWrittenListRequest();
        request.setMemberId(1L);

        ProfileWrittenListResponse item = new ProfileWrittenListResponse();
        item.setId(3L);
        item.setTitle("질문글 테스트");

        when(profileContentMapper.selectProfileWrittenListQuestion(request)).thenReturn(List.of(item));

        List<ProfileWrittenListResponse> result = myContentService.getProfileWrittenListQuestion(request);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("질문글 테스트", result.get(0).getTitle());
        verify(profileContentMapper, times(1)).selectProfileWrittenListQuestion(request);
    }

    @Test
    @DisplayName("내가 쓴 나눔글 삭제 테스트")
    void deleteProfileWrittenSharingTest() {
        ProfileWrittenDeleteRequest request = ProfileWrittenDeleteRequest.builder()
                .memberId(1L)
                .sharingIds(Arrays.asList(1L, 2L))
                .build();

        when(profileContentMapper.deleteProfileWrittenSharing(request)).thenReturn(2);

        boolean result = myContentService.deleteProfileWrittenSharing(request);

        assertTrue(result);
        verify(profileContentMapper, times(1)).deleteProfileWrittenSharing(request);
    }

    @Test
    @DisplayName("내가 쓴 질문글 삭제 테스트")
    void deleteProfileWrittenQuestionTest() {
        ProfileWrittenDeleteRequest request = ProfileWrittenDeleteRequest.builder()
                .memberId(1L)
                .questionIds(Arrays.asList(5L, 6L))
                .build();

        when(profileContentMapper.deleteProfileWrittenQuestion(request)).thenReturn(2);

        boolean result = myContentService.deleteProfileWrittenQuestion(request);

        assertTrue(result);
        verify(profileContentMapper, times(1)).deleteProfileWrittenQuestion(request);
    }

    @Test
    @DisplayName("댓글 단 글 전체 검색 테스트")
    void searchProfileCommentAllTest() {
        ProfileWrittenListRequest request = new ProfileWrittenListRequest();
        request.setMemberId(1L);
        request.setKeyword("테스트");

        ProfileWrittenListResponse comment = new ProfileWrittenListResponse();
        comment.setId(10L);
        comment.setTitle("댓글 테스트");

        when(profileContentMapper.selectProfileCommentSearchAll(request)).thenReturn(List.of(comment));

        List<ProfileWrittenListResponse> result = myContentService.searchProfileCommentAll(request);

        assertNotNull(result);
        assertEquals("댓글 테스트", result.get(0).getTitle());
        verify(profileContentMapper, times(1)).selectProfileCommentSearchAll(request);
    }

    @Test
    @DisplayName("댓글 단 나눔글 검색 테스트")
    void searchProfileCommentSharingTest() {
        ProfileWrittenListRequest request = new ProfileWrittenListRequest();
        request.setMemberId(1L);

        when(profileContentMapper.selectProfileCommentSearchSharing(request))
                .thenReturn(List.of(new ProfileWrittenListResponse()));

        List<ProfileWrittenListResponse> result = myContentService.searchProfileCommentSharing(request);

        assertEquals(1, result.size());
        verify(profileContentMapper, times(1)).selectProfileCommentSearchSharing(request);
    }

    @Test
    @DisplayName("댓글 단 질문글 검색 테스트")
    void searchProfileCommentQuestionTest() {
        ProfileWrittenListRequest request = new ProfileWrittenListRequest();
        request.setMemberId(1L);

        when(profileContentMapper.selectProfileCommentSearchQuestion(request))
                .thenReturn(List.of(new ProfileWrittenListResponse()));

        List<ProfileWrittenListResponse> result = myContentService.searchProfileCommentQuestion(request);

        assertEquals(1, result.size());
        verify(profileContentMapper, times(1)).selectProfileCommentSearchQuestion(request);
    }
}