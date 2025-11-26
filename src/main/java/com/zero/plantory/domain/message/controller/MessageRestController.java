package com.zero.plantory.domain.message.controller;

import com.zero.plantory.domain.message.dto.MessageListResponse;
import com.zero.plantory.domain.message.dto.SearchMessageRequest;
import com.zero.plantory.domain.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/message")
public class MessageRestController {

    private final MessageService messageService;

    @GetMapping("/{memberId}/{boxType}")
    public ResponseEntity<List<MessageListResponse>> getMessageList(
            @PathVariable Long memberId,
            @PathVariable String boxType,
            @RequestParam(required = false) String targetType,
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "20") int limit) {

        SearchMessageRequest req =
                new SearchMessageRequest(memberId, boxType, targetType, title, offset, limit);

        return ResponseEntity.ok(messageService.getMessageList(req));
    }
}
