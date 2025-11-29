package com.zero.plantory.domain.member.mapper;

import com.zero.plantory.domain.profile.dto.ProfileWrittenDeleteRequest;
import com.zero.plantory.domain.profile.dto.ProfileWrittenListRequest;
import com.zero.plantory.domain.profile.dto.ProfileWrittenListResponse;
import com.zero.plantory.domain.profile.mapper.ProfileContentMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class ProfileContentMapperTest {

    @Autowired
    ProfileContentMapper profileContentMapper;

    @Test
    @DisplayName("내가 쓴 글 전체 조회")
    void selectProfileWrittenListAllTest() {
        ProfileWrittenListRequest request = ProfileWrittenListRequest.builder()
                .memberId(1L)
                .keyword("분양")
                .limit(10)
                .offset(0)
                .build();

        var result = profileContentMapper.selectProfileWrittenListAll(request);
        assertNotNull(result);
    }

    @Test
    @DisplayName("내가 쓴 나눔글 조회")
    void selectProfileWrittenListSharingTest() {
        ProfileWrittenListRequest request = ProfileWrittenListRequest.builder()
                .memberId(1L)
                .keyword("분양")
                .limit(10)
                .offset(0)
                .build();

        var result = profileContentMapper.selectProfileWrittenListSharing(request);
        assertNotNull(result);
    }

    @Test
    @DisplayName("내가 쓴 나눔글 조회")
    void selectProfileWrittenListQuestionTest() {
        ProfileWrittenListRequest request = ProfileWrittenListRequest.builder()
                .memberId(1L)
                .keyword("초록")
                .limit(10)
                .offset(0)
                .build();

        var result = profileContentMapper.selectProfileWrittenListQuestion(request);
        assertNotNull(result);
    }

    @Test
    @DisplayName("체크박스로 선택된 나눔글 삭제")
    void deleteProfileWrittenSharingTest() {
        ProfileWrittenDeleteRequest request = ProfileWrittenDeleteRequest.builder()
                .memberId(1L)
                .sharingIds(List.of(1L, 2L))
                .build();

        int deleted = profileContentMapper.deleteProfileWrittenSharing(request);
        assertTrue(deleted >= 0);
    }

    @Test
    @DisplayName("체크박스로 선택된 질문글 삭제")
    void deleteProfileWrittenQuestionTest() {
        ProfileWrittenDeleteRequest request = ProfileWrittenDeleteRequest.builder()
                .memberId(1L)
                .questionIds(List.of(1L, 2L))
                .build();

        int deleted = profileContentMapper.deleteProfileWrittenQuestion(request);
        assertTrue(deleted >= 0);
    }

    @Test
    @DisplayName("댓글 단 글 검색 - 전체")
    void selectProfileCommentSearchAllTest() {
        ProfileWrittenListRequest request = ProfileWrittenListRequest.builder()
                .memberId(1L)
                .keyword("드려요")
                .limit(10)
                .offset(0)
                .build();

        List<ProfileWrittenListResponse> result = profileContentMapper.selectProfileCommentSearchAll(request);

        assertNotNull(result);
        assertFalse(result.isEmpty(), "댓글 검색 전체 결과 없음");
        result.forEach(item -> log.info(item.toString()));
    }

    @Test
    @DisplayName("댓글 단 글 검색 - 나눔글만")
    void selectProfileCommentSearchSharingTest() {
        ProfileWrittenListRequest request = ProfileWrittenListRequest.builder()
                .memberId(1L)
                .keyword("드려요")
                .limit(10)
                .offset(0)
                .build();

        List<ProfileWrittenListResponse> result = profileContentMapper.selectProfileCommentSearchSharing(request);

        assertNotNull(result);
        result.forEach(item -> log.info(item.toString()));
    }

    @Test
    @DisplayName("댓글 단 글 검색 - 질문글만")
    void selectProfileCommentSearchQuestionTest() {
        ProfileWrittenListRequest request = ProfileWrittenListRequest.builder()
                .memberId(1L)
                .keyword("꽃")
                .limit(10)
                .offset(0)
                .build();

        List<ProfileWrittenListResponse> result = profileContentMapper.selectProfileCommentSearchQuestion(request);

        assertNotNull(result);
        result.forEach(item -> log.info(item.toString()));
    }
}
