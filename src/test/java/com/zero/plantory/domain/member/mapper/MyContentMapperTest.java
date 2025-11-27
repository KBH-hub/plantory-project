package com.zero.plantory.domain.member.mapper;

import com.zero.plantory.domain.member.dto.MyWrittenDeleteRequest;
import com.zero.plantory.domain.member.dto.MyWrittenListRequest;
import com.zero.plantory.domain.member.dto.MyWrittenListResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class MyContentMapperTest {

    @Autowired
    MyContentMapper myContentMapper;

    @Test
    @DisplayName("내가 쓴 글 전체 조회")
    void selectMyWrittenListAllTest() {
        MyWrittenListRequest request = MyWrittenListRequest.builder()
                .memberId(1L)
                .keyword("분양")
                .limit(10)
                .offset(0)
                .build();

        var result = myContentMapper.selectMyWrittenListAll(request);
        assertNotNull(result);
    }

    @Test
    @DisplayName("내가 쓴 나눔글 조회")
    void selectMyWrittenListSharingTest() {
        MyWrittenListRequest request = MyWrittenListRequest.builder()
                .memberId(1L)
                .keyword("분양")
                .limit(10)
                .offset(0)
                .build();

        var result = myContentMapper.selectMyWrittenListSharing(request);
        assertNotNull(result);
    }

    @Test
    @DisplayName("내가 쓴 나눔글 조회")
    void selectMyWrittenListQuestionTest() {
        MyWrittenListRequest request = MyWrittenListRequest.builder()
                .memberId(1L)
                .keyword("초록")
                .limit(10)
                .offset(0)
                .build();

        var result = myContentMapper.selectMyWrittenListQuestion(request);
        assertNotNull(result);
    }

    @Test
    @DisplayName("체크박스로 선택된 나눔글 삭제")
    void deleteMyWrittenSharingTest() {
        MyWrittenDeleteRequest request = MyWrittenDeleteRequest.builder()
                .memberId(1L)
                .sharingIds(List.of(1L, 2L))
                .build();

        int deleted = myContentMapper.deleteMyWrittenSharing(request);
        assertTrue(deleted >= 0);
    }

    @Test
    @DisplayName("체크박스로 선택된 질문글 삭제")
    void deleteMyWrittenQuestionTest() {
        MyWrittenDeleteRequest request = MyWrittenDeleteRequest.builder()
                .memberId(1L)
                .questionIds(List.of(1L, 2L))
                .build();

        int deleted = myContentMapper.deleteMyWrittenQuestion(request);
        assertTrue(deleted >= 0);
    }

    @Test
    @DisplayName("댓글 단 글 검색 - 전체")
    void selectMyCommentSearchAllTest() {
        MyWrittenListRequest request = MyWrittenListRequest.builder()
                .memberId(1L)
                .keyword("드려요")
                .limit(10)
                .offset(0)
                .build();

        List<MyWrittenListResponse> result = myContentMapper.selectMyCommentSearchAll(request);

        assertNotNull(result);
        assertFalse(result.isEmpty(), "댓글 검색 전체 결과 없음");
        result.forEach(item -> log.info(item.toString()));
    }

    @Test
    @DisplayName("댓글 단 글 검색 - 나눔글만")
    void selectMyCommentSearchSharingTest() {
        MyWrittenListRequest request = MyWrittenListRequest.builder()
                .memberId(1L)
                .keyword("드려요")
                .limit(10)
                .offset(0)
                .build();

        List<MyWrittenListResponse> result = myContentMapper.selectMyCommentSearchSharing(request);

        assertNotNull(result);
        result.forEach(item -> log.info(item.toString()));
    }

    @Test
    @DisplayName("댓글 단 글 검색 - 질문글만")
    void selectMyCommentSearchQuestionTest() {
        MyWrittenListRequest request = MyWrittenListRequest.builder()
                .memberId(1L)
                .keyword("꽃")
                .limit(10)
                .offset(0)
                .build();

        List<MyWrittenListResponse> result = myContentMapper.selectMyCommentSearchQuestion(request);

        assertNotNull(result);
        result.forEach(item -> log.info(item.toString()));
    }
}
