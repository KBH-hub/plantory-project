package com.zero.plantory.domain.question.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QuestionController {
    @GetMapping("/readQuestion/{questionId}")
    public String readQuestion(@PathVariable Long questionId, Model model) {
        model.addAttribute("questionId", questionId);
        return "question/readQuestion";
    }
}
