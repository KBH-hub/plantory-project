package com.zero.plantory.domain.member.service;

import com.zero.plantory.domain.member.dto.MyWrittenDeleteRequest;
import com.zero.plantory.domain.member.dto.MyWrittenListRequest;
import com.zero.plantory.domain.member.dto.MyWrittenListResponse;
import com.zero.plantory.domain.member.mapper.MyContentMapper;
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
class MyContentServiceImplTest {

    @Mock
    private MyContentMapper myContentMapper;

    @InjectMocks
    private MyContentServiceImpl myContentService;

    @Test
    @DisplayName("내가 쓴 글 전체 조회 테스트")
    void getMyWrittenListAllTest() {
        MyWrittenListRequest request = new MyWrittenListRequest();
        request.setMemberId(1L);

        MyWrittenListResponse item = new MyWrittenListResponse();
        item.setId(1L);
        item.setTitle("전체 글 테스트");

        when(myContentMapper.selectMyWrittenListAll(request)).thenReturn(List.of(item));

        List<MyWrittenListResponse> result = myContentService.getMyWrittenListAll(request);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("전체 글 테스트", result.get(0).getTitle());
        verify(myContentMapper, times(1)).selectMyWrittenListAll(request);
    }

    @Test
    @DisplayName("내가 올린 나눔글 조회 테스트")
    void getMyWrittenListSharingTest() {
        MyWrittenListRequest request = new MyWrittenListRequest();
        request.setMemberId(1L);

        MyWrittenListResponse item = new MyWrittenListResponse();
        item.setId(2L);
        item.setTitle("나눔글 테스트");

        when(myContentMapper.selectMyWrittenListSharing(request)).thenReturn(List.of(item));

        List<MyWrittenListResponse> result = myContentService.getMyWrittenListSharing(request);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("나눔글 테스트", result.get(0).getTitle());
        verify(myContentMapper, times(1)).selectMyWrittenListSharing(request);
    }

    @Test
    @DisplayName("내가 쓴 질문글 조회 테스트")
    void getMyWrittenListQuestionTest() {
        MyWrittenListRequest request = new MyWrittenListRequest();
        request.setMemberId(1L);

        MyWrittenListResponse item = new MyWrittenListResponse();
        item.setId(3L);
        item.setTitle("질문글 테스트");

        when(myContentMapper.selectMyWrittenListQuestion(request)).thenReturn(List.of(item));

        List<MyWrittenListResponse> result = myContentService.getMyWrittenListQuestion(request);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("질문글 테스트", result.get(0).getTitle());
        verify(myContentMapper, times(1)).selectMyWrittenListQuestion(request);
    }

    @Test
    @DisplayName("내가 쓴 나눔글 삭제 테스트")
    void deleteMyWrittenSharingTest() {
        MyWrittenDeleteRequest request = MyWrittenDeleteRequest.builder()
                .memberId(1L)
                .sharingIds(Arrays.asList(1L, 2L))
                .build();

        when(myContentMapper.deleteMyWrittenSharing(request)).thenReturn(2);

        boolean result = myContentService.deleteMyWrittenSharing(request);

        assertTrue(result);
        verify(myContentMapper, times(1)).deleteMyWrittenSharing(request);
    }

    @Test
    @DisplayName("내가 쓴 질문글 삭제 테스트")
    void deleteMyWrittenQuestionTest() {
        MyWrittenDeleteRequest request = MyWrittenDeleteRequest.builder()
                .memberId(1L)
                .questionIds(Arrays.asList(5L, 6L))
                .build();

        when(myContentMapper.deleteMyWrittenQuestion(request)).thenReturn(2);

        boolean result = myContentService.deleteMyWrittenQuestion(request);

        assertTrue(result);
        verify(myContentMapper, times(1)).deleteMyWrittenQuestion(request);
    }

    @Test
    @DisplayName("댓글 단 글 전체 검색 테스트")
    void searchMyCommentAllTest() {
        MyWrittenListRequest request = new MyWrittenListRequest();
        request.setMemberId(1L);
        request.setKeyword("테스트");

        MyWrittenListResponse comment = new MyWrittenListResponse();
        comment.setId(10L);
        comment.setTitle("댓글 테스트");

        when(myContentMapper.selectMyCommentSearchAll(request)).thenReturn(List.of(comment));

        List<MyWrittenListResponse> result = myContentService.searchMyCommentAll(request);

        assertNotNull(result);
        assertEquals("댓글 테스트", result.get(0).getTitle());
        verify(myContentMapper, times(1)).selectMyCommentSearchAll(request);
    }

    @Test
    @DisplayName("댓글 단 나눔글 검색 테스트")
    void searchMyCommentSharingTest() {
        MyWrittenListRequest request = new MyWrittenListRequest();
        request.setMemberId(1L);

        when(myContentMapper.selectMyCommentSearchSharing(request))
                .thenReturn(List.of(new MyWrittenListResponse()));

        List<MyWrittenListResponse> result = myContentService.searchMyCommentSharing(request);

        assertEquals(1, result.size());
        verify(myContentMapper, times(1)).selectMyCommentSearchSharing(request);
    }

    @Test
    @DisplayName("댓글 단 질문글 검색 테스트")
    void searchMyCommentQuestionTest() {
        MyWrittenListRequest request = new MyWrittenListRequest();
        request.setMemberId(1L);

        when(myContentMapper.selectMyCommentSearchQuestion(request))
                .thenReturn(List.of(new MyWrittenListResponse()));

        List<MyWrittenListResponse> result = myContentService.searchMyCommentQuestion(request);

        assertEquals(1, result.size());
        verify(myContentMapper, times(1)).selectMyCommentSearchQuestion(request);
    }
}