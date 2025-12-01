package com.zero.plantory.domain.question.controller;

import com.zero.plantory.domain.question.dto.QuestionListPageResponse;
import com.zero.plantory.domain.question.service.QuestionReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionReadRestController {
    private final QuestionReadService questionReadService;

    @GetMapping
    public QuestionListPageResponse getQuestionList(@RequestParam(defaultValue = "1") int page,
                                                    @RequestParam(defaultValue = "10") int size,
                                                    @RequestParam(required = false) String keyword
    ) {
        return questionReadService.getQuestionList(keyword, page, size);
    }
}
