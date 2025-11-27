package com.zero.plantory.domain.message.controller;

import com.zero.plantory.domain.message.dto.MessageListResponse;
import com.zero.plantory.domain.message.dto.MessageSearchRequest;
import com.zero.plantory.domain.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
            @RequestParam int offset,
            @RequestParam int limit) {

        MessageSearchRequest req =
                new MessageSearchRequest(memberId, boxType, targetType, title, offset, limit);

        return ResponseEntity.ok(messageService.getMessageList(req));
    }

    @DeleteMapping("/deleteMessages")
    public ResponseEntity<Map<String, String>> deleteMessages(@RequestBody List<Long> messageIds, Long removerId) {
        int result = messageService.removeMessages(messageIds, removerId);
        if(result > 0) {
            return ResponseEntity.ok().body(Map.of("message", "delete member success"));
        }
        return ResponseEntity.status(400).body(Map.of("message", "delete member fail"));
    }

    @DeleteMapping("/deleteSenderMessages")
    public ResponseEntity<Map<String, String>> deleteSenderMessages(@RequestBody List<Long> messageIds, Long removerId) {
        int result = messageService.removeMessages(messageIds, removerId);
        if(result > 0) {
            return ResponseEntity.ok().body(Map.of("message", "delete member success"));
        }
        return ResponseEntity.status(400).body(Map.of("message", "delete member fail"));
    }

}
